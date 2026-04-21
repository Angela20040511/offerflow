package com.offerflow.service;

import com.offerflow.dto.ResumeCreateVersionDTO;
import com.offerflow.dto.ResumeSaveDTO;
import com.offerflow.entity.Resume;
import com.offerflow.exception.BusinessException;
import com.offerflow.mapper.ResumeMapper;
import com.offerflow.security.SecurityUtils;
import com.offerflow.util.JsonUtils;
import com.offerflow.util.ResumeScoreUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ResumeService {
    private final ResumeMapper resumeMapper;

    @Value("${offerflow.resume-upload-dir:../frontend/public/files/resume}")
    private String resumeUploadDir;

    public Map<String, Object> getMyResume() {
        return toResumeDetail(getOrCreateDefaultResume(SecurityUtils.currentUserId()));
    }

    public List<Map<String, Object>> getMyResumeList() {
        Long userId = SecurityUtils.currentUserId();
        getOrCreateDefaultResume(userId);
        return resumeMapper.selectByUserId(userId).stream().map(this::toResumeDetail).toList();
    }

    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> save(ResumeSaveDTO request) {
        Resume resume = request.getId() == null ? null : resumeMapper.selectById(request.getId());
        if (resume != null && !resume.getUserId().equals(SecurityUtils.currentUserId())) {
            throw new BusinessException(403, "无权编辑这份简历");
        }
        if (resume == null) {
            resume = Resume.builder()
                    .userId(SecurityUtils.currentUserId())
                    .pdfUrl("/files/resume/default-resume.pdf")
                    .isDefault(resumeMapper.countByUserId(SecurityUtils.currentUserId()) == 0 ? 1 : 0)
                    .build();
        }
        Map<String, Object> basicInfo = normalizeBasicInfo(request.getBasicInfo());
        String resumeName = StringUtils.hasText(request.getResumeName()) ? request.getResumeName().trim() : defaultResumeName(request, basicInfo);
        int score = ResumeScoreUtil.compute(
                basicInfo,
                request.getEducationList(),
                request.getExperienceList(),
                request.getProjectList(),
                request.getSkillList(),
                resume.getPdfUrl()
        );
        resume.setTitle(StringUtils.hasText(request.getTitle()) ? request.getTitle().trim() : resumeName);
        resume.setResumeName(resumeName);
        resume.setResumeType(StringUtils.hasText(request.getResumeType()) ? request.getResumeType().trim() : "GENERAL");
        resume.setTargetSubsidiaryId(request.getTargetSubsidiaryId());
        resume.setTargetCategoryId(request.getTargetCategoryId());
        resume.setBasicInfoJson(JsonUtils.toJson(basicInfo));
        resume.setEducationJson(JsonUtils.toJson(request.getEducationList()));
        resume.setExperienceJson(JsonUtils.toJson(request.getExperienceList()));
        resume.setProjectJson(JsonUtils.toJson(request.getProjectList()));
        resume.setSkillsJson(JsonUtils.toJson(request.getSkillList()));
        resume.setTemplateCode(StringUtils.hasText(request.getTemplateCode()) ? request.getTemplateCode() : "classic");
        resume.setCompleteScore(score);
        if (resume.getId() == null) {
            resumeMapper.insertResume(resume);
        } else {
            resumeMapper.updateResume(resume);
        }
        return toResumeDetail(resumeMapper.selectById(resume.getId()));
    }

    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> createVersion(ResumeCreateVersionDTO request) {
        Long userId = SecurityUtils.currentUserId();
        Resume source = getOrCreateDefaultResume(userId);
        String name = resolveResumeName(request.getResumeName(), source.getResumeName());
        Resume created = Resume.builder()
                .userId(userId)
                .title(name)
                .resumeName(name)
                .resumeType(StringUtils.hasText(request.getResumeType()) ? request.getResumeType() : source.getResumeType())
                .targetSubsidiaryId(request.getTargetSubsidiaryId())
                .targetCategoryId(request.getTargetCategoryId())
                .basicInfoJson(source.getBasicInfoJson())
                .educationJson(source.getEducationJson())
                .experienceJson(source.getExperienceJson())
                .projectJson(source.getProjectJson())
                .skillsJson(source.getSkillsJson())
                .templateCode(StringUtils.hasText(request.getTemplateCode()) ? request.getTemplateCode() : source.getTemplateCode())
                .pdfUrl(source.getPdfUrl())
                .completeScore(source.getCompleteScore())
                .isDefault(0)
                .build();
        resumeMapper.insertResume(created);
        return toResumeDetail(resumeMapper.selectById(created.getId()));
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean setDefault(Long resumeId) {
        Resume resume = validateEditable(resumeId);
        resumeMapper.clearDefaultByUserId(resume.getUserId());
        return resumeMapper.setDefaultById(resumeId) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(Long resumeId) {
        Resume resume = validateEditable(resumeId);
        if (resume.getIsDefault() != null && resume.getIsDefault() == 1) {
            throw new BusinessException(400, "默认简历不能删除");
        }
        if (resumeMapper.countApplicationsByResumeId(resumeId) > 0) {
            throw new BusinessException(400, "该简历已用于投递记录，不能删除。你可以新建或重命名其他版本。");
        }
        return resumeMapper.deleteById(resumeId) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> upload(Long resumeId, MultipartFile file) {
        validateEditable(resumeId);
        if (file == null || file.isEmpty()) {
            throw new BusinessException(400, "请上传 PDF 文件");
        }
        String originalFilename = file.getOriginalFilename() == null ? "" : file.getOriginalFilename().trim().toLowerCase();
        String contentType = file.getContentType() == null ? "" : file.getContentType().trim().toLowerCase();
        if (!originalFilename.endsWith(".pdf") && !contentType.contains("pdf")) {
            throw new BusinessException(400, "仅支持上传 PDF 文件");
        }
        try {
            Path dir = Paths.get(resumeUploadDir);
            Files.createDirectories(dir);
            Files.write(dir.resolve(resumeId + ".pdf"), file.getBytes());
        } catch (IOException e) {
            throw new BusinessException(500, "PDF 上传失败");
        }
        String pdfUrl = "/files/resume/" + resumeId + ".pdf";
        resumeMapper.updateResumePdfUrl(resumeId, pdfUrl);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("resumeId", resumeId);
        result.put("pdfUrl", pdfUrl);
        return result;
    }

    public Map<String, Object> getPdf(Long resumeId) {
        Resume resume = validateReadable(resumeId);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("resumeId", resume.getId());
        result.put("resumeName", resume.getResumeName());
        result.put("pdfUrl", resume.getPdfUrl());
        return result;
    }

    public Map<String, Object> getCompleteScore(Long resumeId) {
        Resume resume = validateReadable(resumeId);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("resumeId", resume.getId());
        result.put("completeScore", resume.getCompleteScore());
        return result;
    }

    private Resume getOrCreateDefaultResume(Long userId) {
        Resume resume = resumeMapper.selectDefaultByUserId(userId);
        if (resume != null) {
            return resume;
        }
        Map<String, Object> basicInfo = new LinkedHashMap<>();
        basicInfo.put("name", "");
        basicInfo.put("phone", "");
        basicInfo.put("email", "");
        basicInfo.put("educationLevel", "");
        basicInfo.put("gender", "");
        Resume created = Resume.builder()
                .userId(userId)
                .title("默认简历")
                .resumeName("默认简历")
                .resumeType("GENERAL")
                .basicInfoJson(JsonUtils.toJson(basicInfo))
                .educationJson(JsonUtils.toJson(List.of()))
                .experienceJson(JsonUtils.toJson(List.of()))
                .projectJson(JsonUtils.toJson(List.of()))
                .skillsJson(JsonUtils.toJson(List.of()))
                .templateCode("classic")
                .pdfUrl("/files/resume/default-resume.pdf")
                .completeScore(0)
                .isDefault(1)
                .build();
        resumeMapper.insertResume(created);
        return resumeMapper.selectById(created.getId());
    }

    private Resume validateEditable(Long resumeId) {
        Resume resume = resumeMapper.selectById(resumeId);
        if (resume == null) {
            throw new BusinessException(404, "简历不存在");
        }
        if (!resume.getUserId().equals(SecurityUtils.currentUserId())) {
            throw new BusinessException(403, "无权操作这份简历");
        }
        return resume;
    }

    private Resume validateReadable(Long resumeId) {
        Resume resume = resumeMapper.selectById(resumeId);
        if (resume == null) {
            throw new BusinessException(404, "简历不存在");
        }
        Long currentUserId = SecurityUtils.currentUserId();
        if (!resume.getUserId().equals(currentUserId) && !SecurityUtils.isHrRole(SecurityUtils.currentUser().getRole())) {
            throw new BusinessException(403, "无权查看这份简历");
        }
        return resume;
    }

    private Map<String, Object> toResumeSummary(Resume resume) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", resume.getId());
        result.put("title", resume.getTitle());
        result.put("resumeName", resume.getResumeName());
        result.put("resumeType", resume.getResumeType());
        result.put("targetSubsidiaryId", resume.getTargetSubsidiaryId());
        result.put("targetCategoryId", resume.getTargetCategoryId());
        result.put("templateCode", resume.getTemplateCode());
        result.put("pdfUrl", resume.getPdfUrl());
        result.put("completeScore", resume.getCompleteScore());
        result.put("isDefault", resume.getIsDefault());
        result.put("updateTime", resume.getUpdateTime());
        return result;
    }

    private Map<String, Object> toResumeDetail(Resume resume) {
        Map<String, Object> result = toResumeSummary(resume);
        Map<String, Object> basicInfo = normalizeBasicInfo(JsonUtils.readObject(resume.getBasicInfoJson()));
        List<Map<String, Object>> educationList = JsonUtils.readObjectList(resume.getEducationJson());
        List<Map<String, Object>> experienceList = JsonUtils.readObjectList(resume.getExperienceJson());
        List<Map<String, Object>> projectList = JsonUtils.readObjectList(resume.getProjectJson());
        List<String> skillList = JsonUtils.readStringList(resume.getSkillsJson());
        result.put("userId", resume.getUserId());
        result.put("basicInfo", basicInfo);
        result.put("educationList", educationList);
        result.put("experienceList", experienceList);
        result.put("projectList", projectList);
        result.put("skillList", skillList);
        result.put("skills", Map.of("jobSkills", skillList, "customSkills", List.of()));
        return result;
    }

    private Map<String, Object> normalizeBasicInfo(Map<String, Object> basicInfo) {
        Map<String, Object> result = new LinkedHashMap<>(basicInfo == null ? Map.of() : basicInfo);
        result.put("name", stringValue(result.get("name")));
        result.put("phone", stringValue(result.get("phone")));
        result.put("email", stringValue(result.get("email")));
        result.put("educationLevel", stringValue(result.get("educationLevel")));
        result.put("gender", stringValue(result.get("gender")));
        return result;
    }

    private String defaultResumeName(ResumeSaveDTO request, Map<String, Object> basicInfo) {
        if (StringUtils.hasText(request.getTitle())) {
            return request.getTitle().trim();
        }
        Object name = basicInfo.get("name");
        if (name != null && StringUtils.hasText(String.valueOf(name))) {
            return String.valueOf(name).trim() + "简历";
        }
        return "默认简历";
    }

    private String resolveResumeName(String requestedName, String fallbackName) {
        if (StringUtils.hasText(requestedName)) {
            return requestedName.trim();
        }
        return StringUtils.hasText(fallbackName) ? fallbackName + "-副本" : "新建简历";
    }

    private String stringValue(Object value) {
        return value == null ? "" : String.valueOf(value).trim();
    }
}

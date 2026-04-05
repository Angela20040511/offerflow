package com.offerflow.service;

import com.offerflow.common.PageResult;
import com.offerflow.dto.ApiModels;
import com.offerflow.dto.CandidateQueryDTO;
import com.offerflow.dto.CandidateStageUpdateDTO;
import com.offerflow.dto.HrNoteSaveDTO;
import com.offerflow.dto.HrReviewScoreDTO;
import com.offerflow.entity.Resume;
import com.offerflow.exception.BusinessException;
import com.offerflow.mapper.ApplicationMapper;
import com.offerflow.mapper.ResumeMapper;
import com.offerflow.security.SecurityUtils;
import com.offerflow.util.JsonUtils;
import com.offerflow.util.MatchScoreUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CandidateService {
    private final ApplicationMapper applicationMapper;
    private final ResumeMapper resumeMapper;

    public PageResult<Map<String, Object>> candidatePage(CandidateQueryDTO query) {
        SecurityUtils.requireRole("HR");
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("keyword", query.getKeyword());
        params.put("subsidiaryId", query.getSubsidiaryId());
        params.put("jobId", query.getJobId());
        params.put("school", query.getSchool());
        params.put("major", query.getMajor());
        params.put("stage", query.getStage());
        params.put("scoreMin", query.getScoreMin());
        params.put("scoreMax", query.getScoreMax());
        params.put("provinceCode", query.getProvinceCode());
        params.put("cityCode", query.getCityCode());
        params.put("offset", (query.getPageNum() - 1) * query.getPageSize());
        params.put("pageSize", query.getPageSize());
        long total = applicationMapper.countCandidatePage(params);
        List<Map<String, Object>> list = applicationMapper.selectCandidatePage(params);
        return new PageResult<>(list, total, query.getPageNum(), query.getPageSize());
    }

    public Map<String, Object> candidateDetail(Long applicationId) {
        SecurityUtils.requireRole("HR");
        Map<String, Object> detail = applicationMapper.selectCandidateDetailByApplicationId(applicationId);
        if (detail == null) {
            throw new BusinessException(404, "候选人记录不存在");
        }
        detail.put("keywordHits", matchScore(applicationId).get("keywordHits"));
        return detail;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean updateCandidateStage(CandidateStageUpdateDTO request) {
        SecurityUtils.requireRole("HR");
        return applicationMapper.updateApplicationStage(request.getApplicationId(), request.getStage()) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean updateCandidateNote(HrNoteSaveDTO request) {
        SecurityUtils.requireRole("HR");
        return applicationMapper.updateHrNote(request.getApplicationId(), request.getHrNote()) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean updateReviewScore(HrReviewScoreDTO request) {
        SecurityUtils.requireRole("HR");
        return applicationMapper.updateHrReview(request.getApplicationId(), request.getHrReviewScore(), request.getHrReviewStatus(), request.getHrNote()) > 0;
    }

    public Map<String, Object> matchScore(Long applicationId) {
        SecurityUtils.requireRole("HR");
        Map<String, Object> detail = applicationMapper.selectCandidateDetailByApplicationId(applicationId);
        if (detail == null) {
            throw new BusinessException(404, "候选人记录不存在");
        }
        Resume resume = resumeMapper.selectById(asLong(detail.get("resumeId")));
        Map<String, Object> basicInfo = new LinkedHashMap<>();
        basicInfo.put("major", detail.getOrDefault("major", ""));
        if (resume != null) {
            basicInfo.putAll(JsonUtils.readObject(resume.getBasicInfoJson()));
        }
        return MatchScoreUtil.buildDetail(
                stringValue(detail.get("jobTitle")),
                stringValue(detail.get("categoryName")),
                stringValue(detail.get("businessLine")),
                JsonUtils.readStringList(stringValue(detail.get("requiredSkillsJson"))),
                basicInfo,
                JsonUtils.readObjectList(stringValue(detail.get("projectJson"))),
                JsonUtils.readObjectList(stringValue(detail.get("experienceJson"))),
                JsonUtils.readStringList(stringValue(detail.get("resumeSkillsJson")))
        );
    }

    public PageResult<Map<String, Object>> applicationPage(ApiModels.HrApplicationPageQuery query) {
        SecurityUtils.requireRole("HR");
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("jobId", query.getJobId());
        params.put("subsidiaryId", query.getSubsidiaryId());
        params.put("stage", query.getStage());
        params.put("keyword", query.getKeyword());
        params.put("offset", (query.getPageNum() - 1) * query.getPageSize());
        params.put("pageSize", query.getPageSize());
        long total = applicationMapper.countHrApplicationPage(params);
        List<Map<String, Object>> list = applicationMapper.selectHrApplicationPage(params);
        return new PageResult<>(list, total, query.getPageNum(), query.getPageSize());
    }

    private Long asLong(Object value) {
        if (value instanceof Number number) {
            return number.longValue();
        }
        return Long.parseLong(String.valueOf(value));
    }

    private String stringValue(Object value) {
        return value == null ? null : String.valueOf(value);
    }
}

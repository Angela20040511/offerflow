package com.offerflow.service;

import com.offerflow.common.PageResult;
import com.offerflow.dto.ApiModels;
import com.offerflow.entity.Application;
import com.offerflow.entity.Favorite;
import com.offerflow.entity.Job;
import com.offerflow.entity.Resume;
import com.offerflow.exception.BusinessException;
import com.offerflow.mapper.ApplicationMapper;
import com.offerflow.mapper.FavoriteMapper;
import com.offerflow.mapper.JobMapper;
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
public class ApplicationService {
    private static final String STAGE_APPLIED = "APPLIED";
    private static final String STAGE_WITHDRAWN = "WITHDRAWN";

    private final ApplicationMapper applicationMapper;
    private final FavoriteMapper favoriteMapper;
    private final ResumeMapper resumeMapper;
    private final JobMapper jobMapper;
    private final JobService jobService;

    public Map<String, Object> overview() {
        Long userId = SecurityUtils.currentUserId();
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("applicationCount", applicationMapper.countApplicantApplications(userId));
        result.put("interviewCount", applicationMapper.countApplicantInterview(userId));

        Map<String, Object> favoriteParams = new LinkedHashMap<>();
        favoriteParams.put("userId", userId);
        favoriteParams.put("keyword", null);
        favoriteParams.put("city", null);
        favoriteParams.put("subsidiaryId", null);
        favoriteParams.put("status", null);
        result.put("favoriteCount", favoriteMapper.countMyFavoritePage(favoriteParams));

        Resume resume = resumeMapper.selectDefaultByUserId(userId);
        result.put("resumeCompleteScore", resume == null ? 0 : resume.getCompleteScore());

        Map<String, Object> applicationParams = new LinkedHashMap<>();
        applicationParams.put("userId", userId);
        applicationParams.put("stage", null);
        applicationParams.put("keyword", null);
        applicationParams.put("subsidiaryId", null);
        applicationParams.put("offset", 0);
        applicationParams.put("pageSize", 5);
        result.put("recentApplications", applicationMapper.selectMyApplicationPage(applicationParams));
        result.put("recommendJobs", jobService.recommendJobs(userId, 4));
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> apply(ApiModels.ApplicationCreateRequest request) {
        Long userId = SecurityUtils.currentUserId();
        Resume resume = resumeMapper.selectById(request.getResumeId());
        if (resume == null || !resume.getUserId().equals(userId)) {
            throw new BusinessException(404, "简历不存在或无权使用");
        }
        Job job = jobMapper.selectById(request.getJobId());
        if (job == null || !"OPEN".equals(job.getStatus())) {
            throw new BusinessException(404, "岗位不存在或已停止招聘");
        }

        Application existing = applicationMapper.selectByUserIdAndJobId(userId, request.getJobId());
        if (existing != null && !STAGE_WITHDRAWN.equals(existing.getStage())) {
            throw new BusinessException(409, "该岗位已投递，请勿重复提交");
        }

        Map<String, Object> basicInfo = JsonUtils.readObject(resume.getBasicInfoJson());
        Map<String, Object> matchDetail = MatchScoreUtil.buildDetail(
                job.getTitle(),
                job.getCategory(),
                job.getBusinessLine(),
                JsonUtils.readStringList(job.getRequiredSkillsJson()),
                basicInfo,
                JsonUtils.readObjectList(resume.getProjectJson()),
                JsonUtils.readObjectList(resume.getExperienceJson()),
                JsonUtils.readStringList(resume.getSkillsJson())
        );
        Integer systemScore = (Integer) matchDetail.get("totalScore");

        Application saved;
        if (existing != null) {
            applicationMapper.reapplyApplication(existing.getId(), request.getResumeId(), systemScore, systemScore);
            saved = applicationMapper.selectById(existing.getId());
        } else {
            Application application = Application.builder()
                    .userId(userId)
                    .jobId(request.getJobId())
                    .resumeId(request.getResumeId())
                    .stage(STAGE_APPLIED)
                    .matchScore(systemScore)
                    .systemMatchScore(systemScore)
                    .build();
            applicationMapper.insertApplication(application);
            saved = applicationMapper.selectById(application.getId());
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("applicationId", saved.getId());
        result.put("stage", saved.getStage());
        result.put("systemMatchScore", saved.getSystemMatchScore());
        result.put("applyTime", saved.getApplyTime());
        return result;
    }

    public PageResult<Map<String, Object>> myApplications(ApiModels.ApplicationPageQuery query) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("userId", SecurityUtils.currentUserId());
        params.put("stage", query.getStage());
        params.put("keyword", query.getKeyword());
        params.put("subsidiaryId", query.getSubsidiaryId());
        params.put("offset", (query.getPageNum() - 1) * query.getPageSize());
        params.put("pageSize", query.getPageSize());
        long total = applicationMapper.countMyApplicationPage(params);
        List<Map<String, Object>> list = applicationMapper.selectMyApplicationPage(params);
        return new PageResult<>(list, total, query.getPageNum(), query.getPageSize());
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean withdraw(Long applicationId) {
        return applicationMapper.withdrawApplication(applicationId, SecurityUtils.currentUserId()) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean addFavorite(ApiModels.FavoriteCreateRequest request) {
        Long userId = SecurityUtils.currentUserId();
        if (favoriteMapper.existsFavorite(userId, request.getJobId()) > 0) {
            return true;
        }
        favoriteMapper.insertFavorite(Favorite.builder().userId(userId).jobId(request.getJobId()).build());
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean removeFavorite(Long jobId) {
        favoriteMapper.deleteFavorite(SecurityUtils.currentUserId(), jobId);
        return true;
    }

    public PageResult<Map<String, Object>> myFavorites(ApiModels.FavoritePageQuery query) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("userId", SecurityUtils.currentUserId());
        params.put("keyword", query.getKeyword());
        params.put("city", query.getCity());
        params.put("subsidiaryId", query.getSubsidiaryId());
        params.put("status", query.getStatus());
        params.put("offset", (query.getPageNum() - 1) * query.getPageSize());
        params.put("pageSize", query.getPageSize());
        long total = favoriteMapper.countMyFavoritePage(params);
        List<Map<String, Object>> list = favoriteMapper.selectMyFavoritePage(params);
        return new PageResult<>(list, total, query.getPageNum(), query.getPageSize());
    }
}

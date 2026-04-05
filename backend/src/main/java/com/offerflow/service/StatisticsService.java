package com.offerflow.service;

import com.offerflow.dto.ApiModels;
import com.offerflow.entity.Interview;
import com.offerflow.exception.BusinessException;
import com.offerflow.mapper.ApplicationMapper;
import com.offerflow.mapper.InterviewMapper;
import com.offerflow.mapper.JobMapper;
import com.offerflow.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final ApplicationMapper applicationMapper;
    private final JobMapper jobMapper;
    private final InterviewMapper interviewMapper;

    public Map<String, Object> dashboardOverview() {
        SecurityUtils.requireRole("HR");
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("openJobCount", jobMapper.countOpenJobs());
        result.put("newApplicationCount", applicationMapper.countNewApplicationsToday());
        result.put("screeningCount", applicationMapper.countByStage("SCREENING"));
        result.put("interviewCount", applicationMapper.countByStage("INTERVIEW"));
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("jobId", null);
        params.put("subsidiaryId", null);
        params.put("stage", null);
        params.put("keyword", null);
        params.put("offset", 0);
        params.put("pageSize", 5);
        result.put("latestApplications", applicationMapper.selectHrApplicationPage(params));
        result.put("subsidiaryJobDistribution", jobDistribution());
        result.put("subsidiaryApplicationDistribution", subsidiaryDistribution());
        return result;
    }

    public Map<String, Object> statisticsOverview() {
        SecurityUtils.requireRole("HR");
        Map<String, Object> result = new LinkedHashMap<>();
        long applied = applicationMapper.countByStage("APPLIED");
        long screening = applicationMapper.countByStage("SCREENING");
        long interview = applicationMapper.countByStage("INTERVIEW");
        long offer = applicationMapper.countByStage("OFFER");
        long rejected = applicationMapper.countByStage("REJECTED");
        long withdrawn = applicationMapper.countByStage("WITHDRAWN");
        result.put("openJobCount", jobMapper.countOpenJobs());
        result.put("applicationCount", applied + screening + interview + offer + rejected + withdrawn);
        result.put("screeningCount", screening);
        result.put("interviewCount", interview);
        result.put("offerCount", offer);
        result.put("rejectedCount", rejected);
        result.put("stagePie", stagePie());
        result.put("subsidiaryDistribution", subsidiaryDistribution());
        result.put("jobDistribution", jobDistribution());
        return result;
    }

    public List<Map<String, Object>> funnel() {
        SecurityUtils.requireRole("HR");
        return stagePie();
    }

    public List<Map<String, Object>> jobDistribution() {
        SecurityUtils.requireRole("HR");
        return jobMapper.selectSubsidiaryJobDistribution();
    }

    public List<Map<String, Object>> stagePie() {
        SecurityUtils.requireRole("HR");
        return applicationMapper.selectStageStats();
    }

    public List<Map<String, Object>> subsidiaryDistribution() {
        SecurityUtils.requireRole("HR");
        return applicationMapper.selectSubsidiaryApplicationDistribution();
    }

    @Transactional(rollbackFor = Exception.class)
    public Long createInterview(ApiModels.InterviewCreateRequest request) {
        SecurityUtils.requireRole("HR");
        if (applicationMapper.selectById(request.getApplicationId()) == null) {
            throw new BusinessException(404, "投递记录不存在");
        }
        Interview interview = Interview.builder()
                .applicationId(request.getApplicationId())
                .roundNum(request.getRoundNum())
                .interviewTime(request.getInterviewTime())
                .interviewer(request.getInterviewer())
                .status("PLANNED")
                .remark("")
                .build();
        interviewMapper.insertInterview(interview);
        return interview.getId();
    }

    public List<Map<String, Object>> interviews(Long applicationId) {
        SecurityUtils.requireRole("HR");
        return interviewMapper.selectByApplicationId(applicationId).stream().map(interview -> {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("id", interview.getId());
            item.put("applicationId", interview.getApplicationId());
            item.put("roundNum", interview.getRoundNum());
            item.put("interviewTime", interview.getInterviewTime());
            item.put("interviewer", interview.getInterviewer());
            item.put("status", interview.getStatus());
            item.put("remark", interview.getRemark());
            return item;
        }).toList();
    }
}

package com.offerflow.service;

import com.offerflow.common.PageResult;
import com.offerflow.dto.ApiModels;
import com.offerflow.dto.CandidateQueryDTO;
import com.offerflow.dto.CandidateStageUpdateDTO;
import com.offerflow.dto.HrNoteSaveDTO;
import com.offerflow.dto.HrReviewScoreDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HrService {
    private final CandidateService candidateService;
    private final StatisticsService statisticsService;

    public Map<String, Object> dashboardOverview() {
        return statisticsService.dashboardOverview();
    }

    public PageResult<Map<String, Object>> candidatePage(CandidateQueryDTO query) {
        return candidateService.candidatePage(query);
    }

    public Map<String, Object> candidateDetail(Long applicationId) {
        return candidateService.candidateDetail(applicationId);
    }

    public Boolean updateCandidateStage(CandidateStageUpdateDTO request) {
        return candidateService.updateCandidateStage(request);
    }

    public Boolean updateCandidateNote(HrNoteSaveDTO request) {
        return candidateService.updateCandidateNote(request);
    }

    public Boolean updateReviewScore(HrReviewScoreDTO request) {
        return candidateService.updateReviewScore(request);
    }

    public Map<String, Object> matchScore(Long applicationId) {
        return candidateService.matchScore(applicationId);
    }

    public PageResult<Map<String, Object>> applicationPage(ApiModels.HrApplicationPageQuery query) {
        return candidateService.applicationPage(query);
    }

    public Map<String, Object> statisticsOverview() {
        return statisticsService.statisticsOverview();
    }

    public List<Map<String, Object>> funnel() {
        return statisticsService.funnel();
    }

    public List<Map<String, Object>> jobDistribution() {
        return statisticsService.jobDistribution();
    }

    public List<Map<String, Object>> stagePie() {
        return statisticsService.stagePie();
    }

    public List<Map<String, Object>> subsidiaryDistribution() {
        return statisticsService.subsidiaryDistribution();
    }

    public Long createInterview(ApiModels.InterviewCreateRequest request) {
        return statisticsService.createInterview(request);
    }

    public List<Map<String, Object>> interviews(Long applicationId) {
        return statisticsService.interviews(applicationId);
    }
}

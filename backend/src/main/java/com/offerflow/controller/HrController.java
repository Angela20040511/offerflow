package com.offerflow.controller;

import com.offerflow.common.ApiResponse;
import com.offerflow.common.PageResult;
import com.offerflow.dto.ApiModels;
import com.offerflow.dto.CandidateQueryDTO;
import com.offerflow.dto.CandidateStageUpdateDTO;
import com.offerflow.dto.HrNoteSaveDTO;
import com.offerflow.dto.HrReviewScoreDTO;
import com.offerflow.service.CandidateService;
import com.offerflow.service.StatisticsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class HrController {
    private final CandidateService candidateService;
    private final StatisticsService statisticsService;

    @GetMapping("/api/hr/dashboard/overview")
    public ApiResponse<Map<String, Object>> dashboardOverview() {
        return ApiResponse.success(statisticsService.dashboardOverview());
    }

    @GetMapping("/api/hr/candidates")
    public ApiResponse<PageResult<Map<String, Object>>> candidatePage(@Valid CandidateQueryDTO query) {
        return ApiResponse.success(candidateService.candidatePage(query));
    }

    @GetMapping("/api/hr/candidates/{applicationId}")
    public ApiResponse<Map<String, Object>> candidateDetail(@PathVariable Long applicationId) {
        return ApiResponse.success(candidateService.candidateDetail(applicationId));
    }

    @PutMapping("/api/hr/candidates/stage")
    public ApiResponse<Boolean> updateCandidateStage(@Valid @RequestBody CandidateStageUpdateDTO request) {
        return ApiResponse.success("候选人阶段已更新", candidateService.updateCandidateStage(request));
    }

    @PutMapping("/api/hr/candidates/note")
    public ApiResponse<Boolean> updateCandidateNote(@Valid @RequestBody HrNoteSaveDTO request) {
        return ApiResponse.success("HR 备注已保存", candidateService.updateCandidateNote(request));
    }

    @PutMapping("/api/hr/candidates/review-score")
    public ApiResponse<Boolean> updateReviewScore(@Valid @RequestBody HrReviewScoreDTO request) {
        return ApiResponse.success("HR 评审结果已保存", candidateService.updateReviewScore(request));
    }

    @GetMapping("/api/hr/candidates/{applicationId}/match-score")
    public ApiResponse<Map<String, Object>> matchScore(@PathVariable Long applicationId) {
        return ApiResponse.success(candidateService.matchScore(applicationId));
    }

    @GetMapping("/api/hr/applications")
    public ApiResponse<PageResult<Map<String, Object>>> hrApplications(@Valid ApiModels.HrApplicationPageQuery query) {
        return ApiResponse.success(candidateService.applicationPage(query));
    }

    @GetMapping("/api/hr/statistics/overview")
    public ApiResponse<Map<String, Object>> statisticsOverview() {
        return ApiResponse.success(statisticsService.statisticsOverview());
    }

    @GetMapping("/api/hr/statistics/funnel")
    public ApiResponse<List<Map<String, Object>>> funnel() {
        return ApiResponse.success(statisticsService.funnel());
    }

    @GetMapping("/api/hr/statistics/job-distribution")
    public ApiResponse<List<Map<String, Object>>> jobDistribution() {
        return ApiResponse.success(statisticsService.jobDistribution());
    }

    @GetMapping("/api/hr/statistics/stage-pie")
    public ApiResponse<List<Map<String, Object>>> stagePie() {
        return ApiResponse.success(statisticsService.stagePie());
    }

    @GetMapping("/api/hr/statistics/subsidiary-distribution")
    public ApiResponse<List<Map<String, Object>>> subsidiaryDistribution() {
        return ApiResponse.success(statisticsService.subsidiaryDistribution());
    }

    @PostMapping("/api/hr/interviews")
    public ApiResponse<Map<String, Long>> createInterview(@Valid @RequestBody ApiModels.InterviewCreateRequest request) {
        return ApiResponse.success("面试安排已创建", Map.of("id", statisticsService.createInterview(request)));
    }

    @GetMapping("/api/hr/interviews")
    public ApiResponse<List<Map<String, Object>>> interviews(@RequestParam("applicationId") Long applicationId) {
        return ApiResponse.success(statisticsService.interviews(applicationId));
    }
}

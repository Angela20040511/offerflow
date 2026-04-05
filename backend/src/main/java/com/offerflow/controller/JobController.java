package com.offerflow.controller;

import com.offerflow.common.ApiResponse;
import com.offerflow.common.PageResult;
import com.offerflow.dto.ApiModels;
import com.offerflow.dto.JobQueryDTO;
import com.offerflow.dto.JobSaveDTO;
import com.offerflow.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class JobController {
    private final JobService jobService;

    @GetMapping("/api/jobs")
    public ApiResponse<PageResult<Map<String, Object>>> page(@Valid JobQueryDTO query) {
        return ApiResponse.success(jobService.page(query));
    }

    @GetMapping("/api/jobs/{id}")
    public ApiResponse<Map<String, Object>> detail(@PathVariable Long id) {
        return ApiResponse.success(jobService.detail(id));
    }

    @GetMapping("/api/hr/jobs")
    public ApiResponse<PageResult<Map<String, Object>>> hrPage(@Valid JobQueryDTO query) {
        return ApiResponse.success(jobService.hrPage(query));
    }

    @PostMapping("/api/hr/jobs")
    public ApiResponse<Map<String, Long>> create(@Valid @RequestBody JobSaveDTO request) {
        return ApiResponse.success("岗位创建成功", Map.of("id", jobService.create(request)));
    }

    @PutMapping("/api/hr/jobs/{id}")
    public ApiResponse<Boolean> update(@PathVariable Long id, @Valid @RequestBody JobSaveDTO request) {
        return ApiResponse.success("岗位更新成功", jobService.update(id, request));
    }

    @DeleteMapping("/api/hr/jobs/{id}")
    public ApiResponse<Boolean> delete(@PathVariable Long id) {
        return ApiResponse.success("岗位删除成功", jobService.delete(id));
    }

    @PutMapping("/api/hr/jobs/{id}/status")
    public ApiResponse<Boolean> updateStatus(@PathVariable Long id, @Valid @RequestBody ApiModels.JobStatusRequest request) {
        return ApiResponse.success("岗位状态已更新", jobService.updateStatus(id, request.getStatus()));
    }
}

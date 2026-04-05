package com.offerflow.controller;

import com.offerflow.common.ApiResponse;
import com.offerflow.common.PageResult;
import com.offerflow.dto.ApiModels;
import com.offerflow.service.ApplicationService;
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
public class ApplicantController {
    private final ApplicationService applicationService;

    @GetMapping("/api/dashboard/applicant/overview")
    public ApiResponse<Map<String, Object>> overview() {
        return ApiResponse.success(applicationService.overview());
    }

    @PostMapping("/api/applications")
    public ApiResponse<Map<String, Object>> apply(@Valid @RequestBody ApiModels.ApplicationCreateRequest request) {
        return ApiResponse.success("投递成功", applicationService.apply(request));
    }

    @GetMapping("/api/applications/me")
    public ApiResponse<PageResult<Map<String, Object>>> myApplications(@Valid ApiModels.ApplicationPageQuery query) {
        return ApiResponse.success(applicationService.myApplications(query));
    }

    @PutMapping("/api/applications/{id}/withdraw")
    public ApiResponse<Boolean> withdraw(@PathVariable Long id) {
        return ApiResponse.success("撤回投递成功", applicationService.withdraw(id));
    }

    @PostMapping("/api/favorites")
    public ApiResponse<Boolean> addFavorite(@Valid @RequestBody ApiModels.FavoriteCreateRequest request) {
        return ApiResponse.success("收藏成功", applicationService.addFavorite(request));
    }

    @DeleteMapping("/api/favorites/{jobId}")
    public ApiResponse<Boolean> removeFavorite(@PathVariable Long jobId) {
        return ApiResponse.success("取消收藏成功", applicationService.removeFavorite(jobId));
    }

    @GetMapping("/api/favorites/me")
    public ApiResponse<PageResult<Map<String, Object>>> myFavorites(@Valid ApiModels.FavoritePageQuery query) {
        return ApiResponse.success(applicationService.myFavorites(query));
    }
}

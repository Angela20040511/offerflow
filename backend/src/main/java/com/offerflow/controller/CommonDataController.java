package com.offerflow.controller;

import com.offerflow.common.ApiResponse;
import com.offerflow.service.CommonDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/common")
@RequiredArgsConstructor
public class CommonDataController {
    private final CommonDataService commonDataService;

    @GetMapping("/subsidiaries")
    public ApiResponse<List<Map<String, Object>>> subsidiaries() {
        return ApiResponse.success(commonDataService.listSubsidiaries());
    }

    @GetMapping("/job-categories")
    public ApiResponse<List<Map<String, Object>>> jobCategories() {
        return ApiResponse.success(commonDataService.listJobCategories());
    }

    @GetMapping("/provinces")
    public ApiResponse<List<Map<String, Object>>> provinces() {
        return ApiResponse.success(commonDataService.listProvinces());
    }

    @GetMapping("/cities")
    public ApiResponse<List<Map<String, Object>>> cities(@RequestParam(value = "provinceCode", required = false) String provinceCode) {
        return ApiResponse.success(commonDataService.listCities(provinceCode));
    }
}

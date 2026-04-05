package com.offerflow.controller;

import com.offerflow.common.ApiResponse;
import com.offerflow.service.GuideService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/guides")
@RequiredArgsConstructor
public class GuideController {
    private final GuideService guideService;

    @GetMapping
    public ApiResponse<List<Map<String, Object>>> list() {
        return ApiResponse.success(guideService.list());
    }

    @GetMapping("/{code}")
    public ApiResponse<Map<String, Object>> detail(@PathVariable String code) {
        return ApiResponse.success(guideService.detail(code));
    }
}

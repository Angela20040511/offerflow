package com.offerflow.controller;

import com.offerflow.common.ApiResponse;
import com.offerflow.dto.ResumeCreateVersionDTO;
import com.offerflow.dto.ResumeSaveDTO;
import com.offerflow.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/resumes")
@RequiredArgsConstructor
public class ResumeController {
    private final ResumeService resumeService;

    @GetMapping("/me")
    public ApiResponse<Map<String, Object>> myResume() {
        return ApiResponse.success(resumeService.getMyResume());
    }

    @GetMapping("/my-list")
    public ApiResponse<List<Map<String, Object>>> myResumeList() {
        return ApiResponse.success(resumeService.getMyResumeList());
    }

    @PostMapping("/save")
    public ApiResponse<Map<String, Object>> save(@RequestBody ResumeSaveDTO request) {
        return ApiResponse.success("简历保存成功", resumeService.save(request));
    }

    @PostMapping("/create-version")
    public ApiResponse<Map<String, Object>> createVersion(@RequestBody ResumeCreateVersionDTO request) {
        return ApiResponse.success("简历版本创建成功", resumeService.createVersion(request));
    }

    @PutMapping("/{id}/default")
    public ApiResponse<Boolean> setDefault(@PathVariable Long id) {
        return ApiResponse.success("默认简历已更新", resumeService.setDefault(id));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> delete(@PathVariable Long id) {
        return ApiResponse.success("简历版本已删除", resumeService.delete(id));
    }

    @PostMapping("/upload")
    public ApiResponse<Map<String, Object>> upload(@RequestParam("resumeId") Long resumeId, @RequestParam("file") MultipartFile file) {
        return ApiResponse.success("PDF 简历上传成功", resumeService.upload(resumeId, file));
    }

    @GetMapping("/{id}/pdf")
    public ApiResponse<Map<String, Object>> pdf(@PathVariable Long id) {
        return ApiResponse.success(resumeService.getPdf(id));
    }

    @GetMapping("/{id}/complete-score")
    public ApiResponse<Map<String, Object>> completeScore(@PathVariable Long id) {
        return ApiResponse.success(resumeService.getCompleteScore(id));
    }
}

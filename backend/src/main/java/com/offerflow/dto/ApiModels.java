package com.offerflow.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

public final class ApiModels {
    private ApiModels() {
    }

    @Data
    public static class LoginRequest {
        @NotBlank(message = "请输入用户名")
        private String username;

        @NotBlank(message = "请输入密码")
        private String password;

        @NotBlank(message = "请选择角色")
        private String role;
    }

    @Data
    public static class RegisterRequest {
        @NotBlank(message = "请输入用户名")
        private String username;

        @NotBlank(message = "请输入密码")
        private String password;

        @NotBlank(message = "请输入确认密码")
        private String confirmPassword;

        @Email(message = "邮箱格式不正确")
        @NotBlank(message = "请输入邮箱")
        private String email;

        @NotBlank(message = "请输入手机号")
        private String phone;

        @NotBlank(message = "请选择角色")
        private String role;
    }

    @Data
    public static class ApplicationPageQuery {
        @Min(value = 1, message = "页码最小为 1")
        private Integer pageNum = 1;

        @Min(value = 1, message = "每页条数最小为 1")
        private Integer pageSize = 10;

        private String stage;
        private String keyword;
        private Long subsidiaryId;
    }

    @Data
    public static class ApplicationCreateRequest {
        @NotNull(message = "岗位不能为空")
        private Long jobId;

        @NotNull(message = "简历不能为空")
        private Long resumeId;
    }

    @Data
    public static class FavoriteCreateRequest {
        @NotNull(message = "岗位不能为空")
        private Long jobId;
    }

    @Data
    public static class FavoritePageQuery {
        @Min(value = 1, message = "页码最小为 1")
        private Integer pageNum = 1;

        @Min(value = 1, message = "每页条数最小为 1")
        private Integer pageSize = 10;

        private String keyword;
        private String city;
        private Long subsidiaryId;
        private String status;
    }

    @Data
    public static class JobStatusRequest {
        @NotBlank(message = "岗位状态不能为空")
        private String status;
    }

    @Data
    public static class HrApplicationPageQuery {
        @Min(value = 1, message = "页码最小为 1")
        private Integer pageNum = 1;

        @Min(value = 1, message = "每页条数最小为 1")
        private Integer pageSize = 10;

        private Long jobId;
        private Long subsidiaryId;
        private String stage;
        private String keyword;
    }

    @Data
    public static class InterviewCreateRequest {
        @NotNull(message = "投递记录不能为空")
        private Long applicationId;

        @NotNull(message = "面试轮次不能为空")
        private Integer roundNum;

        @NotNull(message = "面试时间不能为空")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime interviewTime;

        @NotBlank(message = "请输入面试官")
        private String interviewer;
    }
}

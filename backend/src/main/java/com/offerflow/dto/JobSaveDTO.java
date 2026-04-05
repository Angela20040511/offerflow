package com.offerflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class JobSaveDTO {
    private Long id;

    @NotBlank(message = "岗位名称不能为空")
    private String title;

    private String company;
    private String city;
    private String category;
    private String type;
    private Long subsidiaryId;
    private Long jobCategoryId;
    private String businessLine;
    private String provinceCode;
    private String cityCode;
    private String workMode;
    private String durationType;
    private String internshipLength;
    private List<String> requiredSkills = new ArrayList<>();
    private List<String> tags = new ArrayList<>();

    @NotNull(message = "薪资下限不能为空")
    private Integer salaryMin;

    @NotNull(message = "薪资上限不能为空")
    private Integer salaryMax;

    @NotBlank(message = "岗位说明不能为空")
    private String description;

    @NotBlank(message = "任职要求不能为空")
    private String requirement;

    @NotBlank(message = "岗位状态不能为空")
    private String status;
}

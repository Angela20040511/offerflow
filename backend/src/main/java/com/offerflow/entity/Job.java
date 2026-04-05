package com.offerflow.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Job {
    private Long id;
    private Long subsidiaryId;
    private Long jobCategoryId;
    private String title;
    private String company;
    private String city;
    private String category;
    private String type;
    private String businessLine;
    private String provinceCode;
    private String cityCode;
    private String workMode;
    private String durationType;
    private String internshipLength;
    private String requiredSkillsJson;
    private String tags;
    private Integer salaryMin;
    private Integer salaryMax;
    private String description;
    private String requirement;
    private String status;
    private LocalDateTime publishTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

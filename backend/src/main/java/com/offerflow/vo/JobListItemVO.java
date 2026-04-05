package com.offerflow.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class JobListItemVO {
    private Long id;
    private String groupName;
    private Long subsidiaryId;
    private String subsidiaryName;
    private String shortName;
    private String businessLine;
    private Long jobCategoryId;
    private String categoryName;
    private String provinceCode;
    private String provinceName;
    private String cityCode;
    private String cityName;
    private String workMode;
    private String durationType;
    private String internshipLength;
    private String title;
    private String company;
    private String city;
    private String category;
    private String type;
    private Integer salaryMin;
    private Integer salaryMax;
    private List<String> tags = new ArrayList<>();
    private List<String> requiredSkills = new ArrayList<>();
    private String status;
    private Boolean isFavorite;
    private Boolean isApplied;
    private LocalDateTime publishTime;
}

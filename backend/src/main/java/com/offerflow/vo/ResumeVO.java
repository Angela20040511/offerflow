package com.offerflow.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
public class ResumeVO {
    private Long id;
    private Long userId;
    private String title;
    private String resumeName;
    private String resumeType;
    private Long targetSubsidiaryId;
    private String targetSubsidiaryName;
    private Long targetCategoryId;
    private String targetCategoryName;
    private Map<String, Object> basicInfo = new LinkedHashMap<>();
    private List<Map<String, Object>> educationList = new ArrayList<>();
    private List<Map<String, Object>> experienceList = new ArrayList<>();
    private List<Map<String, Object>> projectList = new ArrayList<>();
    private List<String> skillList = new ArrayList<>();
    private String templateCode;
    private String pdfUrl;
    private Integer completeScore;
    private Integer isDefault;
    private LocalDateTime updateTime;
}

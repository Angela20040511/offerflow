package com.offerflow.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
public class ResumeSaveDTO {
    private Long id;
    private String title;
    private String resumeName;
    private String resumeType = "GENERAL";
    private Long targetSubsidiaryId;
    private Long targetCategoryId;
    private Map<String, Object> basicInfo = new LinkedHashMap<>();
    private List<Map<String, Object>> educationList = new ArrayList<>();
    private List<Map<String, Object>> experienceList = new ArrayList<>();
    private List<Map<String, Object>> projectList = new ArrayList<>();
    private List<String> skillList = new ArrayList<>();
    private String templateCode = "classic";
}

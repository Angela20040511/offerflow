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
public class Resume {
    private Long id;
    private Long userId;
    private String title;
    private String resumeName;
    private String resumeType;
    private Long targetSubsidiaryId;
    private Long targetCategoryId;
    private String basicInfoJson;
    private String educationJson;
    private String experienceJson;
    private String projectJson;
    private String skillsJson;
    private String templateCode;
    private String pdfUrl;
    private Integer completeScore;
    private Integer isDefault;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

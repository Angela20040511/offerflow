package com.offerflow.dto;

import lombok.Data;

@Data
public class ResumeCreateVersionDTO {
    private String resumeName;
    private String resumeType = "GENERAL";
    private Long targetSubsidiaryId;
    private Long targetCategoryId;
    private String templateCode = "classic";
}

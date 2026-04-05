package com.offerflow.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApplicationListItemVO {
    private Long id;
    private Long applicationId;
    private Long jobId;
    private String jobTitle;
    private String groupName;
    private Long subsidiaryId;
    private String subsidiaryName;
    private String businessLine;
    private Long resumeId;
    private String resumeName;
    private String stage;
    private Integer matchScore;
    private Integer systemMatchScore;
    private Integer hrReviewScore;
    private String hrReviewStatus;
    private String hrNote;
    private LocalDateTime applyTime;
    private LocalDateTime updateTime;
}

package com.offerflow.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CandidateListItemVO {
    private Long applicationId;
    private Long candidateId;
    private String candidateName;
    private Long jobId;
    private String jobTitle;
    private Long subsidiaryId;
    private String subsidiaryName;
    private String businessLine;
    private String school;
    private String major;
    private String stage;
    private Integer matchScore;
    private Integer systemMatchScore;
    private Integer hrReviewScore;
    private LocalDateTime applyTime;
}

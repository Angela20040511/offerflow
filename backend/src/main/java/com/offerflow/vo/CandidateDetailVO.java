package com.offerflow.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class CandidateDetailVO {
    private Long applicationId;
    private Long candidateId;
    private String candidateName;
    private String email;
    private String phone;
    private String school;
    private String major;
    private String groupName;
    private Long subsidiaryId;
    private String subsidiaryName;
    private String businessLine;
    private Long jobId;
    private String jobTitle;
    private Long jobCategoryId;
    private String categoryName;
    private String provinceName;
    private String cityName;
    private String stage;
    private Integer matchScore;
    private Integer systemMatchScore;
    private Integer hrReviewScore;
    private String hrReviewStatus;
    private String hrNote;
    private Long resumeId;
    private String resumeName;
    private String resumePdfUrl;
    private List<String> keywordHits = new ArrayList<>();
    private LocalDateTime applyTime;
    private LocalDateTime updateTime;
}

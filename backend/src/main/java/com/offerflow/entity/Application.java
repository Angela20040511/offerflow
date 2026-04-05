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
public class Application {
    private Long id;
    private Long userId;
    private Long jobId;
    private Long resumeId;
    private String stage;
    private String hrNote;
    private Integer matchScore;
    private Integer systemMatchScore;
    private Integer hrReviewScore;
    private String hrReviewStatus;
    private LocalDateTime hrReviewTime;
    private LocalDateTime applyTime;
    private LocalDateTime updateTime;
}

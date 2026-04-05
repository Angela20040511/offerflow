package com.offerflow.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DashboardApplicantVO {
    private Long applicationCount;
    private Long interviewCount;
    private Long favoriteCount;
    private Integer resumeCompleteScore;
    private List<ApplicationListItemVO> recentApplications = new ArrayList<>();
    private List<JobListItemVO> recommendJobs = new ArrayList<>();
}

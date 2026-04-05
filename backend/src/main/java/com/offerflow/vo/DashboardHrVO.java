package com.offerflow.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DashboardHrVO {
    private Long openJobCount;
    private Long newApplicationCount;
    private Long screeningCount;
    private Long interviewCount;
    private List<ApplicationListItemVO> recentApplications = new ArrayList<>();
    private List<SubsidiaryDistributionVO> subsidiaryJobDistribution = new ArrayList<>();
    private List<SubsidiaryDistributionVO> subsidiaryApplicationDistribution = new ArrayList<>();
}

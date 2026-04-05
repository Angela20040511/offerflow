package com.offerflow.vo;

import lombok.Data;

@Data
public class StatisticsOverviewVO {
    private Long openJobCount;
    private Long applicationCount;
    private Long screeningCount;
    private Long interviewCount;
    private Long offerCount;
    private Long rejectedCount;
}

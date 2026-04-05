package com.offerflow.vo;

import lombok.Data;

@Data
public class StagePieVO {
    private String stage;
    private String stageLabel;
    private Long count;
    private Double percentage;
}

package com.offerflow.vo;

import lombok.Data;

@Data
public class SubsidiaryDistributionVO {
    private Long subsidiaryId;
    private String subsidiaryName;
    private Long value;
    private Long jobCount;
    private Long applicationCount;
    private Double percentage;
}

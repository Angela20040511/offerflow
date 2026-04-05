package com.offerflow.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class JobDetailVO extends JobListItemVO {
    private String description;
    private String requirement;
}

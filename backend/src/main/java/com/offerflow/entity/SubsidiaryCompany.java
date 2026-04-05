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
public class SubsidiaryCompany {
    private Long id;
    private String groupName;
    private String subsidiaryName;
    private String shortName;
    private String businessLine;
    private String intro;
    private Integer status;
    private Integer sortNum;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

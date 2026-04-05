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
public class GuideDoc {
    private Long id;
    private String code;
    private String title;
    private String fileUrl;
    private Integer sortNum;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
package com.offerflow.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class HrReviewScoreDTO {
    @NotNull(message = "投递记录不能为空")
    private Long applicationId;

    @NotNull(message = "HR 评估分不能为空")
    private Integer hrReviewScore;

    private String hrReviewStatus;
    private String hrNote;
}

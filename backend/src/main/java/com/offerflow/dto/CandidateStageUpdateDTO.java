package com.offerflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CandidateStageUpdateDTO {
    @NotNull(message = "投递记录不能为空")
    private Long applicationId;

    @NotBlank(message = "候选人阶段不能为空")
    private String stage;
}

package com.offerflow.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class CandidateQueryDTO {
    @Min(value = 1, message = "页码最小为 1")
    private Integer pageNum = 1;

    @Min(value = 1, message = "每页条数最小为 1")
    private Integer pageSize = 10;

    private String keyword;
    private Long subsidiaryId;
    private Long jobId;
    private String educationLevel;
    private String stage;
    private Integer scoreMin;
    private Integer scoreMax;
    private String provinceCode;
    private String cityCode;
}

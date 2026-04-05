package com.offerflow.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class JobQueryDTO {
    @Min(value = 1, message = "页码最小为 1")
    private Integer pageNum = 1;

    @Min(value = 1, message = "每页条数最小为 1")
    private Integer pageSize = 10;

    private String keyword;
    private Long subsidiaryId;
    private Long categoryId;
    private String provinceCode;
    private String cityCode;
    private String city;
    private String category;
    private String type;
    private String workMode;
    private String durationType;
    private Integer salaryMin;
    private Integer salaryMax;
    private String status;
    private String sortBy = "latest";
}

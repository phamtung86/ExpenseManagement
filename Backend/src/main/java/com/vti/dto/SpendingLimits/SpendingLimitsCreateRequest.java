package com.vti.dto.SpendingLimits;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class SpendingLimitsCreateRequest {
    private Double limitAmount;
    private String periodType;
    private LocalDate startDate;
    private String note;
    private Integer categoryId;
    private Integer moneySourceId;

    private Integer userId;
}


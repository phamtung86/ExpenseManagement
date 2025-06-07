package com.vti.dto.SpendingLimits;

import lombok.Data;

import java.util.Date;

@Data
public class SpendingLimitsCreateRequest {
    private Double limitAmount;
    private String periodType;
    private Date startDate;
    private String note;
    private Integer categoryId;
    private Integer moneySourceId;

    private Integer userId;
}


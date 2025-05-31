package com.vti.dto.SpendingLimits;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class SpendingLimitsUpdateRequest {
    private Double limitAmount;
    private String periodType;
    private Date startDate;
    private String note;

    @JsonProperty("isActive")
    private boolean isActive;
}


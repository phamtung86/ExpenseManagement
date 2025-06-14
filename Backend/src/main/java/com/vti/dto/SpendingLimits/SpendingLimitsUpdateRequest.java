package com.vti.dto.SpendingLimits;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class SpendingLimitsUpdateRequest {
    private Double limitAmount;
    private String periodType;
    private LocalDate startDate;
    private String note;

    @JsonProperty("isActive")
    private boolean isActive;
}


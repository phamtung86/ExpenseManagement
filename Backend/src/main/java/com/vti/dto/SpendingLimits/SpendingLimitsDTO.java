package com.vti.dto.SpendingLimits;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpendingLimitsDTO {

    private Integer id;

    private Double limitAmount;

    private String periodType;

    private Date startDate;

    private Double actualSpent;

    private String note;

    private boolean isActive;


    private Date createdAt;
    private Date updateAt;

    private Integer categoriesId;
    private String categoriesName;

    private Integer moneySourcesId;
    private String moneySourcesName;

}

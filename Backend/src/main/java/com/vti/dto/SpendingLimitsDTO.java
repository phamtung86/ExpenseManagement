package com.vti.dto;

import com.vti.entity.Categories;
import com.vti.entity.MoneySources;
import com.vti.entity.SpendingLimits;
import jakarta.persistence.*;
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

    private SpendingLimits.PeriodType periodType;

    private Date startDate;

    private Double actualSpent;

    private String note;

    private boolean isActive;

    private Date createdAt;

    private Date updateAt;

    private String categoriesId;

    private String categoriesName;

    private Integer moneySourcesId;

    private String moneySourcesName;

}

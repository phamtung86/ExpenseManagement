package com.vti.dto.SpendingLimits;

import com.vti.dto.UserDTO;
import com.vti.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpendingLimitsDTO {

    private Integer id;

    private Double limitAmount;

    private String periodType;

    private LocalDate startDate;

    private Double actualSpent;

    private String note;

    private boolean isActive;

    private LocalDate endDate;

    private Date createdAt;
    private Date updateAt;

    private Integer categoriesId;
    private String categoriesName;

    private Integer moneySourcesId;

    private String moneySourcesName;

    private Integer userId;

    private String userFullName;

}

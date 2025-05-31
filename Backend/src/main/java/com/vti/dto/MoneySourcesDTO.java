package com.vti.dto;

import com.vti.dto.SpendingLimits.SpendingLimitsDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoneySourcesDTO {
    private Integer id;

    private String name;

    private Double currentBalance;

    private String bankName;

    private String walletProvider;

    private String type;

    public boolean isActive;

    private Date createdAt;

    private Date updateAt;

    private List<TransactionsDTO> transactions;

    private List<SpendingLimitsDTO> spendingLimits;
}

package com.vti.dto;

import com.vti.entity.MoneySources;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionsDTO {
    private Integer id;
    private Double amount;

    private String action;

    private Date transactionDate;

    private Date updateAt;

    private UserDTO user;

    private Integer categoriesId;

    private String categoriesName;

    private String transactionTypesId;

    private String transactionTypesName;

    private String transactionTypeType;

    private Integer moneySourcesId;

    private String moneySourcesName;
}

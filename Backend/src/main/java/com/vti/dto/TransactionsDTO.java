package com.vti.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TransactionsDTO {

    private Integer id;

    private Double amount;

    private String action;

    private Date transactionDate;

    private Date updateAt;

    private String description;

    private UserDTO user;

    private Integer categoriesId;

    private String categoriesName;

    private String transactionTypesId;

    private String transactionTypesName;

    private String transactionTypeType;

    private Integer moneySourcesId;

    private String moneySourcesName;
}

package com.vti.form;

import lombok.Data;

import java.util.Date;

@Data
public class CreateTransactionForm {

    private Integer id;

    private Double amount;

    private String action;

    private Date transactionDate;

    private Date updateAt;

    private String description;

    private Integer userId;

    private Integer categoriesId;

    private String transactionTypesId;

    private String transactionTypeType;

    private Integer moneySourcesId;

}

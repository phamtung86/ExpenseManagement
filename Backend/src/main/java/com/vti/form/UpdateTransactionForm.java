package com.vti.form;

import lombok.Getter;

import java.util.Date;

@Getter
public class UpdateTransactionForm {

    private String description;

    private Integer categoriesId;

    private Integer moneySourcesId;

    private double amount;

    private Date transactionDate;
}

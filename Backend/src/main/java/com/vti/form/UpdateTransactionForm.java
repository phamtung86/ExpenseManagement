package com.vti.form;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UpdateTransactionForm {

    private String description;

    private Integer categoriesId;

    private Integer moneySourcesId;

    private double amount;

    private Date transactionDate;

    private Integer userId;
}

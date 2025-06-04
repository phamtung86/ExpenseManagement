package com.vti.form;

import lombok.Getter;

@Getter
public class MoneySourceForm {

    private String name;

    private Double currentBalance;

    private String bankName;

    private String walletProvider;

    private String type;

    private Integer userId;

    public boolean isActive;

}

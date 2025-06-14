package com.vti.dto.filter;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
public class TransactionFilter {

    private Integer userId;

    // Tên loại giao dịch (TransactionTypes.name)
    private String transactionTypesName;

    // Tên danh mục (Categories.name)
    private String categoriesName;

    // Tên nguồn tiền (MoneySources.name)
    private String moneySourceName;

    // Lọc từ ngày (>=)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fromDate;

    // Lọc đến ngày (<=)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date toDate;

    public TransactionFilter() {}

    // Getters & Setters

    public void setTransactionTypesName(String transactionTypesName) {
        this.transactionTypesName = transactionTypesName;
    }

    public void setCategoriesName(String categoriesName) {
        this.categoriesName = categoriesName;
    }

    public void setMoneySourceName(String moneySourceName) {
        this.moneySourceName = moneySourceName;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }
}

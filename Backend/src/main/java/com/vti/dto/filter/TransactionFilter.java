package com.vti.dto.filter;

import java.util.Date;

public class TransactionFilter {

    // Tên loại giao dịch (TransactionTypes.name)
    private String transactionTypesName;

    // Tên danh mục (Categories.name)
    private String categoriesName;

    // Tên nguồn tiền (MoneySources.name)
    private String moneySourceName;

    // Lọc từ ngày (>=)
    private Date fromDate;

    // Lọc đến ngày (<=)
    private Date toDate;

    public TransactionFilter() {}

    // Getters & Setters

    public String getTransactionTypesName() {
        return transactionTypesName;
    }

    public void setTransactionTypesName(String transactionTypesName) {
        this.transactionTypesName = transactionTypesName;
    }

    public String getCategoriesName() {
        return categoriesName;
    }

    public void setCategoriesName(String categoriesName) {
        this.categoriesName = categoriesName;
    }

    public String getMoneySourceName() {
        return moneySourceName;
    }

    public void setMoneySourceName(String moneySourceName) {
        this.moneySourceName = moneySourceName;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }
}

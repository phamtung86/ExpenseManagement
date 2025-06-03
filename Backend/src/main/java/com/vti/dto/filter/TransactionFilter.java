package com.vti.dto.filter;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Lớp DTO dùng để truyền các điều kiện lọc giao dịch từ client.
 * Các trường sẽ được ánh xạ từ query parameters trong API.
 */
public class TransactionFilter {

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

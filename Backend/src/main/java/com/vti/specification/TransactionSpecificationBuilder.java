package com.vti.specification;

import com.vti.dto.filter.TransactionFilter;
import com.vti.entity.Transactions;
import org.springframework.data.jpa.domain.Specification;

public class TransactionSpecificationBuilder {

    private final TransactionFilter filter;

    public TransactionSpecificationBuilder(TransactionFilter filter) {
        this.filter = filter;
    }

    public Specification<Transactions> build() {
        Specification<Transactions> where = Specification.where(null);
        if (filter.getUserId() != null) {
            where = where.and(new TransactionSpecification(new SearchCriteria("user.id", "=", filter.getUserId())));
        }
        if (filter.getTransactionTypesName() != null && !filter.getTransactionTypesName().isBlank()) {
            where = where.and(new TransactionSpecification(new SearchCriteria("transactionTypes.type", "=", filter.getTransactionTypesName())));
        }

        if (filter.getCategoriesName() != null && !filter.getCategoriesName().isBlank()) {
            where = where.and(new TransactionSpecification(new SearchCriteria("categories.name", "=", filter.getCategoriesName())));
        }

        if (filter.getMoneySourceName() != null && !filter.getMoneySourceName().isBlank()) {
            where = where.and(new TransactionSpecification(new SearchCriteria("moneySources.name", "=", filter.getMoneySourceName())));
        }

        if (filter.getFromDate() != null) {
            where = where.and(new TransactionSpecification(new SearchCriteria("transactionDate", ">=", filter.getFromDate())));
        }

        if (filter.getToDate() != null) {
            where = where.and(new TransactionSpecification(new SearchCriteria("transactionDate", "<=", filter.getToDate())));
        }

        return where;
    }
}

package com.vti.specification;

import com.vti.entity.Transactions;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public class TransactionSpecification implements Specification<Transactions> {

    private final SearchCriteria criteria;

    public TransactionSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Transactions> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        Path<?> path;

        // Hỗ trợ truy cập nested field như "transactionTypes.name"
        if (criteria.getKey().contains(".")) {
            String[] parts = criteria.getKey().split("\\.");
            path = root.join(parts[0]).get(parts[1]); // join transactionTypes → get name
        } else {
            path = root.get(criteria.getKey());
        }

        return switch (criteria.getOperation()) {
            case "=" -> builder.equal(path, criteria.getValue());
            case ">=" -> builder.greaterThanOrEqualTo(path.as(Date.class), (Date) criteria.getValue());
            case "<=" -> builder.lessThanOrEqualTo(path.as(Date.class), (Date) criteria.getValue());
            case "Like" -> builder.like(path.as(String.class), "%" + criteria.getValue() + "%");
            default -> null;
        };
    }
}

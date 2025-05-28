package com.vti.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity(name = "transactions")
@Data
@NoArgsConstructor
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "amount")
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "action")
    private Action action;

    private String description;

    @Column(name = "transaction_date")
    private Date transactionDate;

    @Column(name = "update_at")
    private Date updateAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Categories categories;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_type_id", referencedColumnName = "id")
    private TransactionTypes transactionTypes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "money_source_id", referencedColumnName = "id")
    private MoneySources moneySources;

    public enum Action{
        CREATED,
        UPDATED,
        DELETED
    }

}


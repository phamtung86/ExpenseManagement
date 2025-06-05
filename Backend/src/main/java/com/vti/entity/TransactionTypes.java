package com.vti.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity(name = "transaction_types")
@Data
@NoArgsConstructor
public class TransactionTypes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Type type;

    @OneToMany(mappedBy = "transactionTypes", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Categories> categories;

    @OneToMany(mappedBy = "transactionTypes", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Transactions> transactions;


    public enum Type{
        INCOME, // thu
        EXPENSE, // chi
        DEBIT, // nợ
        LOAN, // cho vay
        TRANSFER // chuyển khoản
    }
}

package com.vti.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity(name = "money_source")
@Data
@NoArgsConstructor
public class MoneySources {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(name = "current_balance")
    private Double currentBalance;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "wallet_provider")
    private String walletProvider;

    @Enumerated(EnumType.STRING)
    private MoneySourcesType type;

    @Column(name = "isActive")
    public boolean isActive;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "update_at")
    private Date updateAt;

    @OneToMany(mappedBy = "moneySources", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Transactions> transactions;

    @OneToMany(mappedBy = "moneySources", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<SpendingLimits> spendingLimits;

    public enum MoneySourcesType {
        CASH, BANK, EWALLET, OTHER,
    }
}

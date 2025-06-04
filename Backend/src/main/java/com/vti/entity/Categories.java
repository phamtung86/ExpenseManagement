package com.vti.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity(name = "categories")
@Data
@NoArgsConstructor

public class Categories  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(name = "parent_id")
    private Integer parentId;

    private String icon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_type_id", referencedColumnName = "id")

        @JsonBackReference // tránh loop khi serialize
    private TransactionTypes transactionTypes;

    @OneToMany(mappedBy = "categories", fetch = FetchType.EAGER)
        @JsonBackReference // tránh loop khi serialize
    private List<Transactions> transactions;

//    @OneToMany(mappedBy = "categories", fetch = FetchType.EAGER)
//        @JsonBackReference // tránh loop khi serialize
//
//    @JsonBackReference // tránh loop khi serialize
//    private TransactionTypes transactionTypes;
//
//    @OneToMany(mappedBy = "categories", fetch = FetchType.EAGER)
//    @JsonBackReference // tránh loop khi serialize
//    private List<Transactions> transactions;

    @OneToMany(mappedBy = "categories", fetch = FetchType.EAGER)
    @JsonBackReference // tránh loop khi serialize

    private List<SpendingLimits> spendingLimits;

}

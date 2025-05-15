package com.vti.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity(name = "spending_limits")
@Data
@NoArgsConstructor
public class SpendingLimits {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "limit_amount")
    private Double limitAmount;

    @Column(name = "period_type")
    @Enumerated(EnumType.STRING)
    private PeriodType periodType;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "actual_spent")
    private Double actualSpent;

    private String note;

    private boolean isActive;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "update_at")
    private Date updateAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Categories categories;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "money_source_id", referencedColumnName = "id")
    private MoneySources moneySources;

    public enum PeriodType {
        DAILY, WEEKLY, MONTHLY, QUARTERLY, YEARLY;
    }
}

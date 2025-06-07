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
    private Double limitAmount; //Số tiền hạn mức được phép chi trong một kỳ

    @Column(name = "period_type")
    @Enumerated(EnumType.STRING)
    private PeriodType periodType; //Kỳ hạn của giới hạn: ngày / tuần / tháng / quý / năm (DAILY,...)

    @Column(name = "start_date")
    private Date startDate; //Ngày bắt đầu áp dụng hạn mức

    @Column(name = "actual_spent")
    private Double actualSpent; //	Số tiền thực tế đã chi trong kỳ hạn đó

    private String note; //	Ghi chú thêm

    private boolean isActive; //Đánh dấu hạn mức còn hiệu lực hay không

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "update_at")
    private Date updateAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Categories categories; //Danh mục chi tiêu được gắn hạn mức

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "money_source_id", referencedColumnName = "id")
    private MoneySources moneySources; //	Nguồn tiền được áp dụng cho hạn mức đó

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public enum PeriodType {
        DAILY, WEEKLY, MONTHLY, QUARTERLY, YEARLY;
    }
}

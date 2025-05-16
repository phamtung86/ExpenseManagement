package com.vti.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity(name = "password_reset_token")
@Data
public class PasswordResetToken {
    public static final int EXPIRATION = 60 * 24;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    private String token;

    @Column(name = "expity_date")
    private Date expDate;

    @Column(name = "created_at")
    private Date createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}

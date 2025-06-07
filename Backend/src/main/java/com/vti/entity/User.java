package com.vti.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity(name = "user")
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "password")
    private String password;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "update_at")
    private Date updateAt;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Transactions> transactions;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<PasswordResetToken> passwordResetTokens;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<MoneySources> moneySources;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Categories> categories;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<SpendingLimits> spendingLimits;
}

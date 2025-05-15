package com.vti.repository;

import com.vti.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Integer> {
}

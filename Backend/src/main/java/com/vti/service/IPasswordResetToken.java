package com.vti.service;

import com.vti.entity.PasswordResetToken;

public interface IPasswordResetToken {

    PasswordResetToken findByToken(String token);

    void createNewPasswordToken(PasswordResetToken passwordResetToken);
}

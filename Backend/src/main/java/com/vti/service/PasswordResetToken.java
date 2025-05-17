package com.vti.service;

import com.vti.repository.IPasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PasswordResetToken implements IPasswordResetToken {

    @Autowired
    private IPasswordResetTokenRepository passwordResetTokenRepository;
    @Override
    public com.vti.entity.PasswordResetToken findByToken(String token) {
        return passwordResetTokenRepository.findByToken(token);
    }

    @Override
    public void createNewPasswordToken(com.vti.entity.PasswordResetToken passwordResetToken) {
        passwordResetTokenRepository.save(passwordResetToken);
    }

    @Override
    public void removeToken(String token) {
        passwordResetTokenRepository.removePasswordResetTokensByToken(token);
    }
}

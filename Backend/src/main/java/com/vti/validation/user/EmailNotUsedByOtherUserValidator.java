package com.vti.validation.user;

import com.vti.service.IUserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class EmailNotUsedByOtherUserValidator implements ConstraintValidator<EmailNotUsedByOtherUser, String> {

    @Autowired
    private IUserService userService;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.isEmpty()) {
            return true;
        }
        Integer currentUserId = userService.getCurrentUserId();
        System.out.println(currentUserId);

        return !userService.existsByEmailAndIdNot(email, currentUserId);
    }
}

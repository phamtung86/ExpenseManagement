package com.vti.validation.user;

import com.vti.service.IUserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class PhoneNumberNotUsedByOtherUserValidator implements ConstraintValidator<PhoneNumberNotUsedByOtherUser, String> {

    @Autowired
    private IUserService userService;

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return true;
        }

        Integer currentUserId = userService.getCurrentUserId();

        return !userService.existsByPhoneNumberAndIdNot(phoneNumber, currentUserId);
    }
}

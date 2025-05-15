package com.vti.validation.user;

import com.vti.service.IUserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

public class UserPhoneNumberExistsValidator implements ConstraintValidator<UserPhoneNumberExists, String> {

	@Autowired
	private IUserService service;

	@SuppressWarnings("deprecation")
	@Override
	public boolean isValid(String phoneNumber, ConstraintValidatorContext constraintValidatorContext) {

		if (StringUtils.isEmpty(phoneNumber)) {
			return false;
		}

		return service.isUserExistsByPhoneNumber(phoneNumber);
	}
}
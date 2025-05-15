package com.vti.validation.user;

import com.vti.service.IUserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
public class UserPhoneNumberNotExistsValidator implements ConstraintValidator<UserPhoneNumberNotExists, String> {

	@Autowired
	private IUserService service;

	@SuppressWarnings("deprecation")
	@Override
	public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {

		if (StringUtils.isEmpty(username)) {
			return true;
		}

		return !service.isUserExistsByPhoneNumber(username);
	}
}
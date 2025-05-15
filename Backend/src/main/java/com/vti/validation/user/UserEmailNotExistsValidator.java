package com.vti.validation.user;

import com.vti.service.IUserService;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UserEmailNotExistsValidator implements ConstraintValidator<UserEmailNotExists, String> {

	@Autowired
	private IUserService service;

	@SuppressWarnings("deprecation")
	@Override
	public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {

		if (StringUtils.isEmpty(email)) {
			return true;
		}
		return true;

		//return !service.isAccountExistsByEmail(email);
	}
}
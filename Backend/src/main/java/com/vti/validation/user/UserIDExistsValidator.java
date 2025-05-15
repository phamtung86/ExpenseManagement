package com.vti.validation.user;

import com.vti.service.IUserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class UserIDExistsValidator implements ConstraintValidator<UserIDExists, Integer> {

	@Autowired
	private IUserService service;

	@SuppressWarnings("deprecation")
	@Override
	public boolean isValid(Integer id, ConstraintValidatorContext constraintValidatorContext) {

		if (StringUtils.isEmpty(id.toString())) {
			return true;
		}

		return service.isUserExistsByID(id);
	}
}


package com.vti.service;

import com.vti.entity.User;
import com.vti.form.CreateUserForm;

public interface IUserService {

    public User createUser(CreateUserForm createUserForm);

    boolean isUserExistsByID(Integer id);

    boolean isUserExistsByPhoneNumber(String phoneNumber);

    User findUserByPhoneNumber(String phoneNumber);
}

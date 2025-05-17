package com.vti.service;

import com.vti.entity.User;
import com.vti.form.CreateUserForm;

public interface IUserService {

    User createUser(CreateUserForm createUserForm);

    boolean isUserExistsByID(Integer id);

    boolean isUserExistsByPhoneNumber(String phoneNumber);

    boolean isUserExistsByEmail(String email);

    User findUserByPhoneNumber(String phoneNumber);

    User findUserByEmail(String email);

    void updatePassword(String email, String password);

//    User updateUser(User user);
}

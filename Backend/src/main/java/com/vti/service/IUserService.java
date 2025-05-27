package com.vti.service;

import com.vti.dto.UserDTO;
import com.vti.dto.UserRequestDTO;
import com.vti.dto.UserResponseDTO;
import com.vti.entity.User;
import com.vti.form.CreateUserForm;
import com.vti.form.ChangePasswordForm;

public interface IUserService {

    User createUser(CreateUserForm createUserForm);

    boolean isUserExistsByID(Integer id);

    boolean isUserExistsByPhoneNumber(String phoneNumber);

    boolean isUserExistsByEmail(String email);

    User findUserByPhoneNumber(String phoneNumber);

    User findUserByEmail(String email);

    // hàm xử lý đổi mật khẩu khi quên
    void updatePassword(String email, String password);

    boolean changePassword(Integer id, ChangePasswordForm changePasswordForm);

    UserResponseDTO getUserById(Integer id);

    UserResponseDTO updateUser(Integer id, UserRequestDTO userRequestDTO);
//    User updateUser(User user);
}

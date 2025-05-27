package com.vti.service;

import com.vti.entity.User;
import com.vti.form.CreateUserForm;
import com.vti.form.ChangePasswordForm;
import com.vti.repository.IUserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UserService implements IUserService{

    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public User  createUser(CreateUserForm createUserForm) {
        User user = modelMapper.map(createUserForm, User.class);
        user.setPassword(passwordEncoder.encode(createUserForm.getPassword()));
        user.setCreatedAt(new Date());
        return userRepository.save(user);
    }
    @Override
    public boolean isUserExistsByID(Integer id) {
        return userRepository.existsById(id);
    }

    @Override
    public boolean isUserExistsByPhoneNumber(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }

    @Override
    public boolean isUserExistsByEmail(String email) {
        return userRepository.findByEmail(email) != null;
    }

    @Override
    public User findUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void updatePassword(String email, String password) {
        User user = userRepository.findByEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    @Override
    public boolean changePassword(Integer id, ChangePasswordForm changePasswordForm) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) return false;
        User user = optionalUser.get();
        if(!changePasswordForm.getNewPassword().equals(changePasswordForm.getConfirmPassword())) {
            return false;
        }
        if(!passwordEncoder.matches(changePasswordForm.getOldPassword(), user.getPassword())) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(changePasswordForm.getNewPassword()));
        userRepository.save(user);
        return true;
    }


}

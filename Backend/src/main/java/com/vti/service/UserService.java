package com.vti.service;

import com.vti.entity.User;
import com.vti.form.CreateUserForm;
import com.vti.repository.IUserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService{

    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public User createUser(CreateUserForm createUserForm) {

        return null;
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


}

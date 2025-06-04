package com.vti.service;

import com.vti.dto.UserDTO;


import com.vti.dto.UserRequestDTO;
import com.vti.dto.UserResponseDTO;

import com.vti.entity.User;
import com.vti.form.ChangePasswordForm;
import com.vti.form.CreateUserForm;
import com.vti.form.UpdateUserForm;
import com.vti.jwtutils.CustomUserDetails;
import com.vti.repository.IUserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService implements IUserService {

    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public User createUser(CreateUserForm createUserForm) {
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
        if (!changePasswordForm.getNewPassword().equals(changePasswordForm.getConfirmPassword())) {
            return false;
        }
        if (!passwordEncoder.matches(changePasswordForm.getOldPassword(), user.getPassword())) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(changePasswordForm.getNewPassword()));
        userRepository.save(user);
        return true;
    }

    @Override

    public boolean updateUser(Integer userId, UpdateUserForm updateUserForm) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return false;
        }
        user.setFullName(updateUserForm.getFullName());
        user.setEmail(updateUserForm.getEmail());
        user.setPhoneNumber(updateUserForm.getPhoneNumber());
        user.setUpdateAt(new Date());
        userRepository.save(user);
        return true;
    }

    @Override
    public UserDTO getUser(Integer userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            return modelMapper.map(user, UserDTO.class);
        }
        return null;
    }

    @Override
    public boolean existsByEmailAndIdNot(String email, Integer id) {
        System.out.println(userRepository.existsByEmailAndIdNot(email, id));
        return userRepository.existsByEmailAndIdNot(email, id);
    }

    @Override
    public Integer getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getUserId();
        }

        return null;
    }

    @Override
    public boolean existsByPhoneNumberAndIdNot(String phoneNumber, Integer id) {
        return userRepository.existsByPhoneNumberAndIdNot(phoneNumber, id);
    }
//
//    public UserResponseDTO getUserById(Integer id) {
//        Optional<User> user = userRepository.findById(id);
//        return modelMapper.map(user, UserResponseDTO.class);
//
//    }
//
//    @Override
//    @Transactional
//    public UserResponseDTO updateUser(Integer id, UserRequestDTO userRequestDTO) {
//        Optional<User> user = userRepository.findById(id);
//        if (user.isEmpty()) {
//            throw new RuntimeException("User id khong ton tai");
//        }
//        User userRequest = modelMapper.map(userRequestDTO, User.class);
//        User userNew = user.get();
//        userNew.setPhoneNumber(userRequest.getPhoneNumber());
//        userNew.setFullName(userRequest.getFullName());
//        userNew.setEmail(userRequest.getEmail());
//        userNew.setUpdateAt(new Date());
//        userRepository.save(userNew);
//        return modelMapper.map(userNew, UserResponseDTO.class);
//    }
}

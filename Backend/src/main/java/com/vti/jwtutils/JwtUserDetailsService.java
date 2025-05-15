package com.vti.jwtutils;

import com.vti.entity.User;
import com.vti.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private IUserService userService;

    @Override
    public CustomUserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        User u = userService.findUserByPhoneNumber(phoneNumber);
        if (u == null) {
            throw new UsernameNotFoundException("User not found with phone number: " + phoneNumber);
        } else {
            return new CustomUserDetails(u.getPhoneNumber(), u.getPassword(), AuthorityUtils.createAuthorityList(String.valueOf(u.getId()),u.getPhoneNumber()), u.getId(),u.getFullName());
        }
    }
}

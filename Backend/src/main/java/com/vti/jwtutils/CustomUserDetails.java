package com.vti.jwtutils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class CustomUserDetails extends User {

    private static final long serialVersionUID = 1L;
    private Integer userId;
    private String fullName;
    private String phoneNumber;


    public CustomUserDetails(String password, Collection<? extends GrantedAuthority> authorities, Integer userId,String fullName, String phoneNumber) {
        super(phoneNumber, password, authorities);
        this.userId = userId;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }
}

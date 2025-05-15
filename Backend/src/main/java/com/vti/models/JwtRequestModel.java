package com.vti.models;

import lombok.Data;

import java.io.Serializable;

@Data
public class JwtRequestModel implements Serializable {
    private static final long serialVersionUID = 2636936156391265891L;
    private String phoneNumber;
    private String password;

    public JwtRequestModel(String phoneNumber, String password) {
        super();
        this.phoneNumber = phoneNumber; this.password = password;
    }

}

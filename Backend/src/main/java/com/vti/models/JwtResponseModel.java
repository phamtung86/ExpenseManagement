package com.vti.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class JwtResponseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    private String token;
    private Integer userId;
    private String fullName;
}

package com.vti.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Integer id;

    private String fullName;

    private String email;

    private String phoneNumber;

//    private String password;

    private Date createdAt;

    private Date updateAt;

}

package com.vti.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {
    private Integer id;

    private String fullName;

    private String email;

    private String phoneNumber;

}

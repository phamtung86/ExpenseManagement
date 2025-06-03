package com.vti.form;

import com.vti.validation.user.EmailNotUsedByOtherUser;
import com.vti.validation.user.PhoneNumberNotUsedByOtherUser;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateUserForm {

    @NotBlank
    private String fullName;

    @Email
    @EmailNotUsedByOtherUser
    private String email;

    @PhoneNumberNotUsedByOtherUser
    private String phoneNumber;
}

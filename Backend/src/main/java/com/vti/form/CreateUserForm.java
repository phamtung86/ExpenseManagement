package com.vti.form;

import com.vti.validation.user.UserEmailNotExists;
import com.vti.validation.user.UserPhoneNumberNotExists;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
public class CreateUserForm {

    @NotBlank(message = "{User.createUser.form.fullname.NotBlank}")
    private String fullName;

    @Email(message = "{User.createUser.form.email.Invalid}")
    @UserEmailNotExists
    @NotBlank(message = "{User.createUser.form.email.NotBlank}")
    private String email;

    @UserPhoneNumberNotExists
    @NotBlank(message = "{User.createUser.form.phoneNumber.NotBlank}")
    private String phoneNumber;

    @NotBlank(message = "{User.createUser.form.password.NotBlank}")
    @Length(min = 8, max = 30, message = "{User.createUser.form.password.LengthRange}")
    private String password;


}

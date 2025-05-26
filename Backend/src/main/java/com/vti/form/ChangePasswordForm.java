package com.vti.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
public class ChangePasswordForm {

    @NotBlank(message = "{User.updatePassword.form.password.NotBlank}")
    @Length(min=8, max=30, message = "{User.createUser.form.password.LengthRange}")
    private String oldPassword;

    @NotBlank(message = "{User.updatePassword.form.password.NotBlank}")
    @Length(min=8, max=30, message = "{User.updatePassword.form.password.LengthRange}")
    private String newPassword;

    @NotBlank(message = "{User.updatePassword.form.password.NotBlank}")
    @Length(min=8, max=30, message = "{User.updatePassword.form.password.LengthRange}")
    private String confirmPassword;
}

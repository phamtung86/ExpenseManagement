package com.vti.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {

    @NotNull(message = "ID không được null")
    private Integer id;

    @NotBlank(message = "Họ tên không được để trống")
    @Size(min = 3, max = 50, message = "Họ tên phải chứa từ 3 đến 50 ký tự")
    private String fullName;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    private String email;

    @Pattern(
            regexp = "^\\+?[0-9. ()-]{7,25}$",
            message = "Số điện thoại không hợp lệ"
    )
    private String phoneNumber;
}

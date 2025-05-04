package com.recruitment.identity.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployerRegisterRequest {

    @Email(message = "Email không hợp lệ")
    @NotBlank(message = "Email không được để trống")
    String email;

    @Size(min = 6, message = "Password phải có ít nhất {min} ký tự")
    String password;

    @NotBlank(message = "Họ và tên không được để trống")
    String fullName;

    @NotBlank(message = "Số điện thoại không được để trống")
    String phone;

    @NotBlank(message = "Tên công ty không được để trống")
    @Size(max = 255, message = "Tên công ty không được quá {max} ký tự")
    String companyName;

    @NotBlank(message = "Thành phố công ty không được để trống")
    String companyCity;

}

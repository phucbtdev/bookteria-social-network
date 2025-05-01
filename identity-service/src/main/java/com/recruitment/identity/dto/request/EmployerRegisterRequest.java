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

    @Email(message = "INVALID_EMAIL")
    @NotBlank(message = "EMAIL_IS_REQUIRED")
    String email;

    @Size(min = 6, message = "INVALID_PASSWORD")
    String password;

    @NotBlank(message = "FULLNAME_IS_REQUIRED")
    String fullName;

    @NotBlank(message = "PHONE_IS_REQUIRED")
    String phone;

    @NotBlank(message = "Company name is required")
    @Size(max = 255, message = "Company name must be less than 255 characters")
    String companyName;

    @NotBlank(message = "COMPANYNAME_IS_REQUIRED")
    String companyCity;

}

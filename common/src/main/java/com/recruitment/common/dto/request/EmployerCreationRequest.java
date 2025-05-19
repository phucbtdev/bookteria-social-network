package com.recruitment.common.dto.request;

import java.time.LocalDate;
import java.util.UUID;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployerCreationRequest {
    UUID userId;

    UUID subscriptionId;

    LocalDate packageExpiryDate;

    @NotBlank(message = "Họ và tên không được để trống")
    String fullName;

    @NotBlank(message = "Tên công ty không được để trống")
    @Size(max = 255, message = "Tên công ty không được quá {max} ký tự")
    String companyName;

    @NotBlank(message = "Thành phố công ty không được để trống")
    String companyCity;

    @NotBlank(message = "Số điện thoại là bắt buộc")
    String phone;

    @Size(max = 255, message = "Website URL must be less than 255 characters")
    String companyWebsite;

    String companyLogoUrl;

    @Size(max = 50, message = "Company size must be less than 50 characters")
    String companySize;

    @Size(max = 255, message = "Industry must be less than 255 characters")
    String industry;

    String companyDescription;

}

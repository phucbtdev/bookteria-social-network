package com.recruitment.event.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployerCreationRequest {
    UUID userId;

    Integer currentPackageId;

    LocalDate packageExpiryDate;

    String fullName;

    @NotBlank(message = "Company name is required")
    @Size(max = 255, message = "Company name must be less than 255 characters")
    String companyName;

    @NotBlank(message = "COMPANY_ADDRESS_IS_REQUIRED")
    String companyCity;

    @NotBlank(message = "PHONE_IS_REQUIRED")
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

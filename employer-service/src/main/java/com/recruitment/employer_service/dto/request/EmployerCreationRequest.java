package com.recruitment.employer_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class EmployerCreationRequest {
    UUID userId;

    Integer currentPackageId;

    LocalDate packageExpiryDate;

    @NotBlank(message = "Company name is required")
    @Size(max = 255, message = "Company name must be less than 255 characters")
    String companyName;

    String companyAddress;

    @Size(max = 255, message = "Website URL must be less than 255 characters")
    String companyWebsite;

    String companyLogoUrl;

    @Size(max = 50, message = "Company size must be less than 50 characters")
    String companySize; // Small, Medium, Large

    @Size(max = 255, message = "Industry must be less than 255 characters")
    String industry;

    String companyDescription;


}

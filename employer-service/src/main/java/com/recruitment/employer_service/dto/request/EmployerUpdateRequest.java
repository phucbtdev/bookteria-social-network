package com.recruitment.employer_service.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class EmployerUpdateRequest {
    Integer currentPackageId;

    LocalDate packageExpiryDate;

    @Size(max = 255, message = "Company name must be less than 255 characters")
    String companyName;

    String companyAddress;

    @Size(max = 255, message = "Website URL must be less than 255 characters")
    String companyWebsite;

    String companyLogoUrl;

    @Size(max = 50, message = "Company size must be less than 50 characters")
    String companySize;

    @Size(max = 255, message = "Industry must be less than 255 characters")
    String industry;

    String companyDescription;

    Boolean isVerified;
}

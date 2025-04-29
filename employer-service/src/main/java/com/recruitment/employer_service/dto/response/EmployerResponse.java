package com.recruitment.employer_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class EmployerResponse {
    UUID userId;
    Integer currentPackageId;
    LocalDate packageExpiryDate;
    String companyName;
    String companyAddress;
    String companyWebsite;
    String companyLogoUrl;
    String companySize;
    String industry;
    String companyDescription;
    Boolean isVerified;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}

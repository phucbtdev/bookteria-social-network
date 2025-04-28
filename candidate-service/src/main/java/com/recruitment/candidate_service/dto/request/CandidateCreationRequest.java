package com.recruitment.candidate_service.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;
import java.util.UUID;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CandidateCreationRequest {
    @NotNull
    UUID userId;

    Integer  currentPackageId;

    @FutureOrPresent
    LocalDate packageExpiryDate;

    @NotNull
    @NotBlank(message = "EMAIL_IS_REQUIRED")
    String fullName;

    @URL
    String avatarUrl;

    @URL
    String resumeUrl;

    @URL
    String linkedinUrl;

    @URL
    String portfolioUrl;
}

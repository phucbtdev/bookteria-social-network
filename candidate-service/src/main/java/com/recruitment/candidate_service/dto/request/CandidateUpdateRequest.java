package com.recruitment.candidate_service.dto.request;

import java.time.LocalDate;
import java.util.UUID;

import com.recruitment.candidate_service.entity.CandidatePackage;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CandidateUpdateRequest {

    UUID subscriptionId;

    @FutureOrPresent
    LocalDate packageExpiryDate;

    @NotBlank
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

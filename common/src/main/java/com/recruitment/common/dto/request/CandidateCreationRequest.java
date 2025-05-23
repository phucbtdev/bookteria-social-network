package com.recruitment.common.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

    UUID  subscriptionId;

    @FutureOrPresent
    LocalDate packageExpiryDate;

    @NotBlank(message = "Họ và tên không được để trống")
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

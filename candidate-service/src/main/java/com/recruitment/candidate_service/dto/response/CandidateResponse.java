package com.recruitment.candidate_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CandidateResponse {
    UUID id;
    String userId;
    Short currentPackageId;
    LocalDate packageExpiryDate;
    String fullName;
    String avatarUrl;
    String resumeUrl;
    String linkedinUrl;
    String portfolioUrl;
}

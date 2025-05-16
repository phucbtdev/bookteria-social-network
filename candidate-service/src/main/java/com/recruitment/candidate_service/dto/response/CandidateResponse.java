package com.recruitment.candidate_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CandidateResponse {
    String userId;
    Short currentPackageId;
    LocalDate packageExpiryDate;
    String fullName;
    String avatarUrl;
    String resumeUrl;
    String linkedinUrl;
    String portfolioUrl;
    Boolean deleted;
}

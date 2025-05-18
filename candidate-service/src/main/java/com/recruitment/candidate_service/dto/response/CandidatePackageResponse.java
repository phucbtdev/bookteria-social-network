package com.recruitment.candidate_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CandidatePackageResponse {
    UUID id;
    String name;
    String description;
    BigDecimal price;
    Integer durationDays;
    Integer maxCvs;
    Integer maxJobApplications;
    Boolean featuredCv;
    Boolean aiJobMatching;
    Boolean supportPriority;
    Boolean isActive;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}

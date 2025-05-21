package com.recruitment.job_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobResponse {
    UUID id;

    UUID employerId;

    String title;

    String slug;

    String description;

    UUID industryId;

    UUID jobLevelId;

    UUID experienceLevelId;

    UUID salaryRangeId;

    UUID workTypeId;

    Integer numberOfPositions;

    String skillsRequired;

    GenderRequirement genderRequirement;

    String address;

    BigDecimal latitude;

    BigDecimal longitude;

    LocalDate applicationDeadline;

    LocalDate startDate;

    LocalDate endDate;

    JobPostStatus status;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;

    public enum GenderRequirement {
        ANY, MALE, FEMALE
    }

    public enum JobPostStatus {
        PENDING, APPROVED, REJECTED
    }
}

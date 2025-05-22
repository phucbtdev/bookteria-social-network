package com.recruitment.job_service.dto.request;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobUpdateRequest {
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

    @DecimalMin(value = "-90.0", message = "Latitude must be greater than or equal to -90")
    @DecimalMax(value = "90.0", message = "Latitude must be less than or equal to 90")
    BigDecimal latitude;

    @DecimalMin(value = "-180.0", message = "Longitude must be greater than or equal to -180")
    @DecimalMax(value = "180.0", message = "Longitude must be less than or equal to 180")
    BigDecimal longitude;

    LocalDate applicationDeadline;

    JobPostStatus status;

    public enum GenderRequirement {
        ANY, MALE, FEMALE
    }

    public enum JobPostStatus {
        PENDING, APPROVED, REJECTED
    }
}

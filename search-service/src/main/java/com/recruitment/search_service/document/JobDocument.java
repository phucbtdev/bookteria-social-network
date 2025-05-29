package com.recruitment.search_service.document;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobDocument {
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
    String genderRequirement;
    String address;
    BigDecimal latitude;
    BigDecimal longitude;
    LocalDate applicationDeadline;
    String status;
}
package com.recruitment.search_service.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobSearchRequest {

    String keyword;
    UUID industryId;
    UUID jobLevelId;
    UUID experienceLevelId;
    UUID salaryRangeId;
    UUID workTypeId;
    List<String> skills;
    String genderRequirement;
    String address;
    Double latitude;
    Double longitude;
    Double distance; // in kilometers
    String status;

    // Salary range filters
    Long minSalary;
    Long maxSalary;

    // Experience filters
    Integer minExperience;
    Integer maxExperience;

    // Date filters
    String datePosted; // TODAY, WEEK, MONTH, etc.
}
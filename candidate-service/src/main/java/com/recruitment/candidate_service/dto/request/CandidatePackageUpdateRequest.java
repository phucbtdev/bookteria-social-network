package com.recruitment.candidate_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CandidatePackageUpdateRequest {
    Integer id;
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
}

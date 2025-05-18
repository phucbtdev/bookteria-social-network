package com.recruitment.employer_service.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployerPackageResponse {
    UUID id;
    String name;
    String description;
    Integer price;
    Integer durationDays;
    Integer maxJobPosts;
    Integer maxFeaturedJobs;
    Boolean prioritySupport;
    Boolean isActive;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}

package com.recruitment.employer_service.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployerPackageCreationRequest {
    @NotBlank(message = "Package name is required")
    @Size(max = 100, message = "Package name must be less than 100 characters")
    String name;

    String description;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be greater than or equal to 0")
    Integer price;

    @NotNull(message = "Duration days is required")
    @Min(value = 1, message = "Duration days must be at least 1")
    Integer durationDays;

    @NotNull(message = "Maximum job posts is required")
    @Min(value = 0, message = "Maximum job posts must be greater than or equal to 0")
    Integer maxJobPosts;

    @Min(value = 0, message = "Maximum featured jobs must be greater than or equal to 0")
    Integer maxFeaturedJobs = 0;

    Boolean prioritySupport = false;

    Boolean isActive = true;
}

package com.recruitment.employer_service.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployerPackageSubscriptionCreationRequest {
    @NotNull(message = "Employer ID is required")
    UUID employerId;

    @NotNull(message = "Package ID is required")
    Integer packageId;

    @NotNull(message = "Start date is required")
    LocalDate startDate;

    @NotNull(message = "End date is required")
    LocalDate endDate;

    Boolean isActive;

    String status;
}

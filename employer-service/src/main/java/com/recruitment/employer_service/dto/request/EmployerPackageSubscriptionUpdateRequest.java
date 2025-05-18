package com.recruitment.employer_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployerPackageSubscriptionUpdateRequest {
    UUID employerId;

    UUID packageId;

    LocalDate startDate;

    LocalDate endDate;

    Boolean isActive;

    String status;
}

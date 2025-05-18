package com.recruitment.employer_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployerPackageSubscriptionResponse {
    UUID id;

    UUID employerId;

    UUID packageId;

    LocalDate startDate;

    LocalDate endDate;

    Boolean isActive;

    String status;

    LocalDateTime createdAt;
}

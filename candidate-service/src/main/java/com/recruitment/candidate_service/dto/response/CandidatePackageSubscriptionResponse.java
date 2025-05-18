package com.recruitment.candidate_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class
CandidatePackageSubscriptionResponse {
    UUID id;

    UUID candidateId;

    UUID packageId;

    LocalDate startDate;

    LocalDate endDate;

    BigDecimal amountPaid;

    Integer jobApplicationsUsed;

    String paymentReference;

    SubscriptionStatus status;

    LocalDateTime cancelledAt;


     public enum SubscriptionStatus {
        ACTIVE, EXPIRED, CANCELLED, PENDING
    }
}

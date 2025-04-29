package com.recruitment.candidate_service.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CandidatePackageSubscriptionCreationRequest {
    @NotNull(message = "Subscription code is required")
    UUID subscriptionCode;

    @NotNull(message = "Candidate ID is required")
    UUID candidateId;

    @NotNull(message = "Package ID is required")
    Integer packageId;

    @NotNull(message = "Start date is required")
    LocalDate startDate;

    @NotNull(message = "End date is required")
    LocalDate endDate;

    @NotNull(message = "Amount paid is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Amount paid must be greater than or equal to 0")
    BigDecimal amountPaid;

    @Builder.Default
    Integer jobApplicationsUsed = 0;

    String paymentReference;

    @NotNull(message = "Status is required")
    SubscriptionStatus status;

    public enum SubscriptionStatus {
        ACTIVE, EXPIRED, CANCELLED, PENDING
    }
}

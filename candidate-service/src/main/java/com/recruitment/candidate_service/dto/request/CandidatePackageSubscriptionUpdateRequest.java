package com.recruitment.candidate_service.dto.request;

import com.recruitment.candidate_service.entity.BaseEntity;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CandidatePackageSubscriptionUpdateRequest extends BaseEntity {
    UUID subscriptionCode;

    UUID candidateId;

    Integer packageId;

    LocalDate startDate;

    LocalDate endDate;

    @DecimalMin(value = "0.0", inclusive = true, message = "Amount paid must be greater than or equal to 0")
    BigDecimal amountPaid;

    Integer jobApplicationsUsed;

    String paymentReference;

    SubscriptionStatus status;

    LocalDateTime cancelledAt;

    public enum SubscriptionStatus {
        ACTIVE, EXPIRED, CANCELLED, PENDING
    }
}

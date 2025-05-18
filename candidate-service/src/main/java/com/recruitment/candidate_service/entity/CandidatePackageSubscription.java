package com.recruitment.candidate_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "candidate_package_subscriptions")
public class CandidatePackageSubscription extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id", nullable = false)
    Candidate candidate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id", nullable = false)
    CandidatePackage candidatePackage;

    @Column(name = "start_date", nullable = false)
    LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    LocalDate endDate;

    @Column(nullable = false, precision = 10, scale = 2)
    BigDecimal amountPaid;

    @Builder.Default
    @Column(name = "job_applications_used")
    Integer jobApplicationsUsed = 0;

    @Column(name = "payment_reference", length = 100)
    String paymentReference;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    SubscriptionStatus status;

    @Column(name = "cancelled_at")
     LocalDateTime cancelledAt;


    public enum SubscriptionStatus {
        ACTIVE, EXPIRED, CANCELLED, PENDING
    }
}

package com.recruiment.payment_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Payment {

    @Id
    @GeneratedValue
    UUID paymentId;

    @Column(nullable = false)
    UUID userId;

    // Relationship với Transaction
    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    List<Transaction> transactions = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    UserType userType;

    @Column(nullable = false)
    UUID packageId;

    @Column(nullable = false)
    BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    PaymentStatus status;

    String paymentMethod;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;

    LocalDateTime expiresAt;


    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum UserType {
        CANDIDATE, EMPLOYER
    }

    public enum PaymentStatus {
        PENDING, SUCCESS, FAILED, EXPIRED, CANCELLED
    }

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;

        // Set expiration time (30 minutes from creation)
        if (expiresAt == null) {
            expiresAt = createdAt.plusMinutes(30);
        }
    }

    // Helper methods
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

    public boolean canBeProcessed() {
        return status == PaymentStatus.PENDING && !isExpired();
    }

    // Get the latest successful transaction
    public Transaction getSuccessfulTransaction() {
        return transactions.stream()
                .filter(t -> t.getStatus() == Transaction.TransactionStatus.SUCCESS)
                .findFirst()
                .orElse(null);
    }
}



package com.recruiment.payment_service.dto;

import com.recruiment.payment_service.entity.Transaction;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionResponse {

    UUID transactionId;
    UUID paymentId;
    String gateway;
    String gatewayTransactionId;
    String gatewayOrderId;
    String gatewayResponseCode;
    String gatewayMessage;
    BigDecimal amount;
    Transaction.TransactionStatus status;
    Transaction.TransactionType type;
    String bankCode;
    String cardType;
    LocalDateTime processedAt;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    // Payment info
    PaymentSummary payment;

    @Data
    @Builder
    public static class PaymentSummary {
        UUID paymentId;
        UUID userId;
        String userType;
        UUID packageId;
        String paymentMethod;
        String paymentStatus;
    }
}
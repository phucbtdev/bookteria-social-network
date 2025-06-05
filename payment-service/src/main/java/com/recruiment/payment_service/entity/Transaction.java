package com.recruiment.payment_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Transaction {

    @Id
    String transactionId;

    // Reference to Payment
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    Payment payment;

    // Gateway transaction info
    @Column(nullable = false, length = 50)
    String gateway; // VNPAY, MOMO

    @Column(unique = true, length = 100)
    String gatewayTransactionId; // Transaction ID từ gateway (vnp_TxnRef, transId)

    @Column(length = 100)
    String gatewayOrderId; // Order ID gửi tới gateway

    @Column(length = 10)
    String gatewayResponseCode; // Response code từ gateway (00, 0, etc.)

    @Column(length = 500)
    String gatewayMessage; // Message từ gateway

    // Transaction details
    @Column(nullable = false, precision = 19, scale = 2)
    BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    TransactionStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    TransactionType type;

    // Additional info
    @Column(length = 50)
    String bankCode; // Mã ngân hàng (nếu có)

    @Column(length = 100)
    String cardType; // Loại thẻ (ATM, CREDIT, etc.)

    @Column(columnDefinition = "TEXT")
    String rawCallbackData; // Raw data từ callback để debug

    LocalDateTime processedAt; // Thời gian xử lý giao dịch
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (this.transactionId == null) {
            this.transactionId = UUID.randomUUID().toString(); // Tạo transactionId tự động
        }
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Helper methods
    public boolean isSuccessful() {
        return status == TransactionStatus.SUCCESS;
    }

    public boolean isPending() {
        return status == TransactionStatus.PENDING;
    }

    public boolean isFailed() {
        return status == TransactionStatus.FAILED;
    }

    public enum TransactionStatus {
        PENDING,     // Đang xử lý
        SUCCESS,     // Thành công
        FAILED,      // Thất bại
        CANCELLED,   // Bị hủy
        REFUNDED     // Đã hoàn tiền
    }

    public enum TransactionType {
        PAYMENT,     // Thanh toán
        REFUND,      // Hoàn tiền
        PARTIAL_REFUND // Hoàn tiền một phần
    }
}
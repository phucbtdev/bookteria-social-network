package com.recruiment.payment_service.dto;

import com.recruiment.payment_service.entity.Payment;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Đối tượng trả về thông tin thanh toán (ví dụ link thanh toán, paymentId, trạng thái,...)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentResponse {
    UUID paymentId;
    UUID userId;
    Payment.UserType userType;
    UUID packageId;
    BigDecimal amount;
    Payment.PaymentStatus status;
    String paymentMethod;
    String paymentUrl; // URL để redirect đến gateway thanh toán
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}

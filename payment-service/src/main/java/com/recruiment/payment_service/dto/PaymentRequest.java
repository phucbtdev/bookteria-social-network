package com.recruiment.payment_service.dto;

import com.recruiment.payment_service.entity.Payment;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {
    private UUID userId;
    private Payment.UserType userType;
    private UUID packageId;
    private BigDecimal amount;
    private String paymentMethod;
}


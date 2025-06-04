package com.recruiment.payment_service.dto;

import com.recruiment.payment_service.entity.Payment;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentReturnResponse {

    UUID paymentId;
    Payment.PaymentStatus status;
    String message;
    String redirectUrl; // URL để redirect về frontend
    boolean success;
    String transactionId;
    String gatewayResponse;
}
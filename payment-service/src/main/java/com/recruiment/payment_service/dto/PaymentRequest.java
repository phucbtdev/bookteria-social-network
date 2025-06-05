package com.recruiment.payment_service.dto;

import com.recruiment.payment_service.entity.Payment;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentRequest {
    @NotNull(message = "User ID is required")
    UUID userId;

    @NotNull(message = "User type is required")
    Payment.UserType userType;

    @NotNull(message = "Package ID is required")
    UUID packageId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", inclusive = true, message = "Amount must be greater than 0")
    BigDecimal amount;

    @NotNull(message = "Payment method is required")
    @Pattern(regexp = "^(MOMO|VNPAY)$", message = "Invalid payment method")
    String paymentMethod;

    @URL(message = "Invalid return URL")
    String returnUrl;

    @URL(message = "Invalid notify URL")
    String notifyUrl;
}


package com.recruiment.payment_service.dto;

import com.recruiment.payment_service.entity.Transaction;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionRequest {

    UUID paymentId;
    String gateway;
    String gatewayOrderId;
    BigDecimal amount;
    Transaction.TransactionType type;
    String description;
}
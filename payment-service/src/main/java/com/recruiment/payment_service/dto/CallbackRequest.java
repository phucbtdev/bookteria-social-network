package com.recruiment.payment_service.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CallbackRequest {

    String gateway;
    String rawData;
    Map<String, String> parameters;
    String signature;
    String orderId;
    String transactionId;
    String responseCode;
    String message;
}

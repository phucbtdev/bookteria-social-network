package com.recruiment.payment_service.service.gateway;

import com.recruiment.payment_service.dto.PaymentRequest;
import com.recruiment.payment_service.dto.PaymentReturnResponse;
import com.recruiment.payment_service.entity.Payment;

import java.util.Map;

public interface PaymentGateway {

    String getGatewayName();

    String createPaymentUrl(Payment payment, PaymentRequest request);

    boolean verifyPaymentCallback(String requestData);

    PaymentReturnResponse processPaymentReturn(Map<String, String[]> parameterMap);

    Payment.PaymentStatus getPaymentStatus(String transactionId);

}

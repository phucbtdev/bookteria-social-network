package com.recruiment.payment_service.service;
import com.recruiment.payment_service.dto.PaymentRequest;
import com.recruiment.payment_service.dto.PaymentResponse;
import com.recruiment.payment_service.dto.PaymentReturnResponse;
import com.recruiment.payment_service.entity.Payment;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface PaymentService {

    PaymentResponse createPayment(PaymentRequest request);

    PaymentResponse getPaymentById(UUID paymentId);

    List<PaymentResponse> getPaymentsByUser(UUID userId, Payment.UserType userType);

    PaymentResponse updatePaymentStatus(UUID paymentId, Payment.PaymentStatus status);

    boolean processPaymentCallback(String gateway, String callbackData);

    List<String> getSupportedPaymentMethods();

    PaymentReturnResponse processPaymentReturn(String gateway, Map<String, String[]> parameterMap);
}
package com.recruiment.payment_service.service.gateway;

import com.recruiment.payment_service.dto.PaymentRequest;
import com.recruiment.payment_service.entity.Payment;

public interface PaymentGateway {

    String getGatewayName();

    String createPaymentUrl(Payment payment, PaymentRequest request);

    boolean verifyPaymentCallback(String requestData);

    Payment.PaymentStatus getPaymentStatus(String transactionId);

}

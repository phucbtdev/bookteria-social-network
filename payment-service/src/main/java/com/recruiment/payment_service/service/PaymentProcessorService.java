package com.recruiment.payment_service.service;

import com.recruiment.payment_service.dto.PaymentRequest;
import com.recruiment.payment_service.dto.PaymentResponse;
import com.recruiment.payment_service.gateway.PaymentGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Service xử lý thanh toán
 * Chọn gateway dựa trên paymentMethod của request
 */
@Service
@RequiredArgsConstructor
public class PaymentProcessorService {

    private final Map<String, PaymentGateway> paymentGateways;

    /**
     * Tạo đơn thanh toán theo phương thức đã chọn
     */
    public PaymentResponse createPayment(PaymentRequest request) {
        String paymentMethod = request.getPaymentMethod().toLowerCase();

        PaymentGateway gateway = paymentGateways.get(paymentMethod + "PaymentGateway");
        if (gateway == null) {
            throw new NoSuchElementException("Payment method " + paymentMethod + " not supported");
        }
        return gateway.createPayment(request);
    }

    /**
     * Xác nhận callback thanh toán (ví dụ webhook)
     */
    public boolean verifyCallback(String paymentMethod, String params) {
        PaymentGateway gateway = paymentGateways.get(paymentMethod.toLowerCase() + "PaymentGateway");
        if (gateway == null) {
            throw new NoSuchElementException("Payment method " + paymentMethod + " not supported");
        }
        return gateway.verifyPaymentCallback(params);
    }
}

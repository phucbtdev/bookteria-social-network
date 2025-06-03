package com.recruiment.payment_service.gateway;

import com.recruiment.payment_service.dto.PaymentRequest;
import com.recruiment.payment_service.dto.PaymentResponse;

public interface PaymentGateway {

    /**
     * Tạo đơn thanh toán và trả về link thanh toán hoặc token thanh toán
     */
    PaymentResponse createPayment(PaymentRequest request);

    /**
     * Xác thực callback hoặc webhook từ cổng thanh toán
     * @param params Map dữ liệu callback từ cổng thanh toán
     */
    boolean verifyPaymentCallback(String params);

    // Các phương thức mở rộng khác nếu cần (refund, query status,...)
}

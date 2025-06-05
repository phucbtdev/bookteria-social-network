package com.recruiment.payment_service.service.impl;

import com.recruiment.payment_service.dto.PaymentRequest;
import com.recruiment.payment_service.dto.PaymentResponse;
import com.recruiment.payment_service.dto.PaymentReturnResponse;
import com.recruiment.payment_service.entity.Payment;
import com.recruiment.payment_service.entity.Transaction;
import com.recruiment.payment_service.factory.PaymentGatewayFactory;
import com.recruiment.payment_service.repository.PaymentRepository;
import com.recruiment.payment_service.service.PaymentService;
import com.recruiment.payment_service.service.gateway.PaymentGateway;
import com.recruiment.payment_service.service.gateway.impl.MoMoGateway;
import com.recruiment.payment_service.service.gateway.impl.VNPayGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentGatewayFactory gatewayFactory;

    @Override
    public PaymentResponse createPayment(PaymentRequest request) {
        log.info("Creating payment for user: {} with method: {}", request.getUserId(), request.getPaymentMethod());

        // Validate payment method
        PaymentGateway gateway = gatewayFactory.getGateway(request.getPaymentMethod());

        // Create payment entity
        Payment payment = Payment.builder()
                .userId(request.getUserId())
                .userType(request.getUserType())
                .packageId(request.getPackageId())
                .amount(request.getAmount())
                .status(Payment.PaymentStatus.PENDING)
                .paymentMethod(request.getPaymentMethod().toUpperCase())
                .build();

        // Save payment
        payment = paymentRepository.save(payment);

        // Generate payment URL
        String paymentUrl = gateway.createPaymentUrl(payment, request);

        log.info("Payment created successfully with ID: {}", payment.getPaymentId());

        return PaymentResponse.builder()
                .paymentId(payment.getPaymentId())
                .userId(payment.getUserId())
                .userType(payment.getUserType())
                .packageId(payment.getPackageId())
                .amount(payment.getAmount())
                .status(payment.getStatus())
                .paymentMethod(payment.getPaymentMethod())
                .paymentUrl(paymentUrl)
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentById(UUID paymentId) {
        Payment payment = paymentRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found with ID: " + paymentId));

        return mapToPaymentResponse(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentsByUser(UUID userId, Payment.UserType userType) {
        List<Payment> payments = paymentRepository.findByUserIdAndUserType(userId, userType);

        return payments.stream()
                .map(this::mapToPaymentResponse).toList();
    }

    @Override
    public PaymentResponse updatePaymentStatus(UUID paymentId, Payment.PaymentStatus status) {
        Payment payment = paymentRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found with ID: " + paymentId));

        payment.setStatus(status);
        payment = paymentRepository.save(payment);

        log.info("Payment status updated to {} for payment ID: {}", status, paymentId);

        return mapToPaymentResponse(payment);
    }

    @Override
    public boolean processPaymentCallback(String gatewayName, String callbackData) {
        try {
            PaymentGateway gateway = gatewayFactory.getGateway(gatewayName);
            return gateway.verifyPaymentCallback(callbackData);
        } catch (Exception e) {
            log.error("Error processing payment callback for gateway: {}", gatewayName, e);
            return false;
        }
    }

    public PaymentReturnResponse processPaymentReturn(String gateway, Map<String, String[]> parameterMap) {
        try {
            log.info("Processing payment return for gateway: {}", gateway);

            PaymentGateway gatewayInstance = gatewayFactory.getGateway(gateway);
            return gatewayInstance.processPaymentReturn(parameterMap);

        } catch (Exception e) {
            log.error("Error processing payment return for gateway: {}", gateway, e);
            return PaymentReturnResponse.builder()
                    .success(false)
                    .status(Payment.PaymentStatus.FAILED)
                    .message("Payment processing failed: " + e.getMessage())
                    .build();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getSupportedPaymentMethods() {
        return gatewayFactory.getSupportedGateways();
    }

    private PaymentResponse mapToPaymentResponse(Payment payment) {
        return PaymentResponse.builder()
                .paymentId(payment.getPaymentId())
                .userId(payment.getUserId())
                .userType(payment.getUserType())
                .packageId(payment.getPackageId())
                .amount(payment.getAmount())
                .status(payment.getStatus())
                .paymentMethod(payment.getPaymentMethod())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }
}

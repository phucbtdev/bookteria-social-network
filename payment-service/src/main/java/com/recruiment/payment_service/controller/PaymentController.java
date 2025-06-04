package com.recruiment.payment_service.controller;

import com.recruiment.payment_service.dto.PaymentRequest;
import com.recruiment.payment_service.dto.PaymentResponse;
import com.recruiment.payment_service.dto.PaymentReturnResponse;
import com.recruiment.payment_service.entity.Payment;
import com.recruiment.payment_service.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(@Valid @RequestBody PaymentRequest request) {
        log.info("Received payment creation request for user: {}", request.getUserId());

        try {
            PaymentResponse response = paymentService.createPayment(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            log.error("Invalid payment request: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error creating payment", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponse> getPayment(@PathVariable UUID paymentId) {
        try {
            PaymentResponse response = paymentService.getPaymentById(paymentId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Payment not found: {}", paymentId);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error retrieving payment", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PaymentResponse>> getUserPayments(
            @PathVariable UUID userId,
            @RequestParam Payment.UserType userType) {
        try {
            List<PaymentResponse> payments = paymentService.getPaymentsByUser(userId, userType);
            return ResponseEntity.ok(payments);
        } catch (Exception e) {
            log.error("Error retrieving user payments", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{paymentId}/status")
    public ResponseEntity<PaymentResponse> updatePaymentStatus(
            @PathVariable UUID paymentId,
            @RequestParam Payment.PaymentStatus status) {
        try {
            PaymentResponse response = paymentService.updatePaymentStatus(paymentId, status);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Payment not found: {}", paymentId);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error updating payment status", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // Endpoint xử lý return từ VNPay
    @GetMapping("/return/vnpay")
    public RedirectView handleVNPayReturn(HttpServletRequest request) {
        log.info("Received VNPay return callback");

        try {
            PaymentReturnResponse response = paymentService.processPaymentReturn("VNPAY", request.getParameterMap());

            // Redirect về frontend với kết quả
            String redirectUrl = buildRedirectUrl(response);
            return new RedirectView(redirectUrl);

        } catch (Exception e) {
            log.error("Error processing VNPay return", e);
            // Redirect về trang lỗi
            return new RedirectView("http://localhost:3000/payment/error?message=Payment processing failed");
        }
    }

    // Endpoint xử lý return từ MoMo
    @GetMapping("/return/momo")
    public RedirectView handleMoMoReturn(HttpServletRequest request) {
        log.info("Received MoMo return callback");

        try {
            PaymentReturnResponse response = paymentService.processPaymentReturn("MOMO", request.getParameterMap());

            // Redirect về frontend với kết quả
            String redirectUrl = buildRedirectUrl(response);
            return new RedirectView(redirectUrl);

        } catch (Exception e) {
            log.error("Error processing MoMo return", e);
            // Redirect về trang lỗi
            return new RedirectView("http://localhost:3000/payment/error?message=Payment processing failed");
        }
    }

    // Endpoint xử lý return chung cho tất cả gateway
    @GetMapping("/return")
    public RedirectView handlePaymentReturn(
            @RequestParam String gateway,
            HttpServletRequest request) {
        log.info("Received payment return callback for gateway: {}", gateway);

        try {
            PaymentReturnResponse response = paymentService.processPaymentReturn(gateway.toUpperCase(), request.getParameterMap());

            // Redirect về frontend với kết quả
            String redirectUrl = buildRedirectUrl(response);
            return new RedirectView(redirectUrl);

        } catch (Exception e) {
            log.error("Error processing payment return for gateway: {}", gateway, e);
            // Redirect về trang lỗi
            return new RedirectView("http://localhost:3000/payment/error?message=Payment processing failed");
        }
    }

    // API endpoint để kiểm tra trạng thái payment (cho frontend gọi AJAX)
    @GetMapping("/return/status")
    public ResponseEntity<PaymentReturnResponse> getPaymentReturnStatus(
            @RequestParam String gateway,
            HttpServletRequest request) {
        log.info("Received payment return status check for gateway: {}", gateway);

        try {
            PaymentReturnResponse response = paymentService.processPaymentReturn(gateway.toUpperCase(), request.getParameterMap());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error processing payment return status", e);
            return ResponseEntity.internalServerError()
                    .body(PaymentReturnResponse.builder()
                            .success(false)
                            .message("Payment processing failed")
                            .status(Payment.PaymentStatus.FAILED)
                            .build());
        }
    }

    @PostMapping("/callback/{gateway}")
    public ResponseEntity<String> handlePaymentCallback(
            @PathVariable String gateway,
            @RequestBody String callbackData) {
        try {
            boolean isValid = paymentService.processPaymentCallback(gateway, callbackData);
            if (isValid) {
                return ResponseEntity.ok("Callback processed successfully");
            } else {
                return ResponseEntity.badRequest().body("Invalid callback data");
            }
        } catch (Exception e) {
            log.error("Error processing payment callback", e);
            return ResponseEntity.internalServerError().body("Error processing callback");
        }
    }

    @GetMapping("/methods")
    public ResponseEntity<List<String>> getSupportedPaymentMethods() {
        try {
            List<String> methods = paymentService.getSupportedPaymentMethods();
            return ResponseEntity.ok(methods);
        } catch (Exception e) {
            log.error("Error retrieving payment methods", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    private String buildRedirectUrl(PaymentReturnResponse response) {
        StringBuilder url = new StringBuilder("http://localhost:3000/payment/result");
        url.append("?paymentId=").append(response.getPaymentId());
        url.append("&status=").append(response.getStatus());
        url.append("&success=").append(response.isSuccess());

        if (response.getMessage() != null) {
            url.append("&message=").append(response.getMessage());
        }

        if (response.getTransactionId() != null) {
            url.append("&transactionId=").append(response.getTransactionId());
        }

        return url.toString();
    }
}

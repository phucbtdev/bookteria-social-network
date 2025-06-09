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

/**
 * Lớp Controller này xử lý các yêu cầu liên quan đến thanh toán.
 * Cung cấp các endpoint để tạo thanh toán, lấy thông tin thanh toán,
 * cập nhật trạng thái thanh toán và xử lý callback trả về từ các cổng thanh toán.
 */
@Slf4j
@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * Endpoint để tạo mới một giao dịch thanh toán.
     *
     * @param request đối tượng {@link PaymentRequest} chứa thông tin thanh toán.
     * @return {@link ResponseEntity} chứa kết quả thanh toán dưới dạng {@link PaymentResponse}.
     */
    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(@Valid @RequestBody PaymentRequest request) {
        log.info("Nhận yêu cầu tạo thanh toán cho người dùng: {}", request.getUserId());

        try {
            PaymentResponse response = paymentService.createPayment(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            log.error("Yêu cầu thanh toán không hợp lệ: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Lỗi khi tạo thanh toán", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Endpoint để lấy thông tin thanh toán dựa trên ID thanh toán.
     *
     * @param paymentId ID của thanh toán cần lấy thông tin.
     * @return {@link ResponseEntity} chứa thông tin thanh toán dưới dạng {@link PaymentResponse}.
     */
    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponse> getPayment(@PathVariable UUID paymentId) {
        try {
            PaymentResponse response = paymentService.getPaymentById(paymentId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Không tìm thấy thanh toán với ID: {}", paymentId);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Lỗi khi lấy thông tin thanh toán", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Endpoint để lấy danh sách các thanh toán của người dùng.
     *
     * @param userId   ID của người dùng cần lấy thông tin thanh toán.
     * @param userType Loại người dùng (ví dụ: khách hàng, admin).
     * @return {@link ResponseEntity} chứa danh sách các thanh toán dưới dạng {@link List<PaymentResponse>}.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PaymentResponse>> getUserPayments(
            @PathVariable UUID userId,
            @RequestParam Payment.UserType userType) {
        try {
            List<PaymentResponse> payments = paymentService.getPaymentsByUser(userId, userType);
            return ResponseEntity.ok(payments);
        } catch (Exception e) {
            log.error("Lỗi khi lấy danh sách thanh toán của người dùng", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Endpoint để cập nhật trạng thái của một giao dịch thanh toán.
     *
     * @param paymentId ID của thanh toán cần cập nhật trạng thái.
     * @param status    Trạng thái thanh toán mới.
     * @return {@link ResponseEntity} chứa kết quả thanh toán dưới dạng {@link PaymentResponse}.
     */
    @PutMapping("/{paymentId}/status")
    public ResponseEntity<PaymentResponse> updatePaymentStatus(
            @PathVariable UUID paymentId,
            @RequestParam Payment.PaymentStatus status) {
        try {
            PaymentResponse response = paymentService.updatePaymentStatus(paymentId, status);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Không tìm thấy thanh toán với ID: {}", paymentId);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Lỗi khi cập nhật trạng thái thanh toán", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Endpoint xử lý callback trả về từ cổng thanh toán VNPay.
     *
     * @param request đối tượng {@link HttpServletRequest} chứa thông tin từ callback.
     * @return {@link RedirectView} chuyển hướng đến frontend với kết quả thanh toán.
     */
    @GetMapping("/return/vnpay")
    public RedirectView handleVNPayReturn(HttpServletRequest request) {
        log.info("Nhận callback trả về từ VNPay");

        try {
            PaymentReturnResponse response = paymentService.processPaymentReturn("VNPAY", request.getParameterMap());

            // Chuyển hướng về frontend với kết quả thanh toán
            String redirectUrl = buildRedirectUrl(response);
            return new RedirectView(redirectUrl);

        } catch (Exception e) {
            log.error("Lỗi khi xử lý callback từ VNPay", e);
            // Chuyển hướng về trang lỗi
            return new RedirectView("http://localhost:3000/payment/error?message=Payment processing failed");
        }
    }

    /**
     * Endpoint xử lý callback trả về từ cổng thanh toán MoMo.
     *
     * @param request đối tượng {@link HttpServletRequest} chứa thông tin từ callback.
     * @return {@link RedirectView} chuyển hướng đến frontend với kết quả thanh toán.
     */
    @GetMapping("/return/momo")
    public RedirectView handleMoMoReturn(HttpServletRequest request) {
        log.info("Nhận callback trả về từ MoMo");

        try {
            PaymentReturnResponse response = paymentService.processPaymentReturn("MOMO", request.getParameterMap());

            // Chuyển hướng về frontend với kết quả thanh toán
            String redirectUrl = buildRedirectUrl(response);
            return new RedirectView(redirectUrl);

        } catch (Exception e) {
            log.error("Lỗi khi xử lý callback từ MoMo", e);
            // Chuyển hướng về trang lỗi
            return new RedirectView("http://localhost:3000/payment/error?message=Payment processing failed");
        }
    }

    /**
     * Endpoint xử lý callback từ các cổng thanh toán.
     *
     * @param gateway tên cổng thanh toán.
     * @param callbackData dữ liệu callback từ cổng thanh toán.
     * @return {@link ResponseEntity} với kết quả xử lý callback.
     */
    @PostMapping("/callback/{gateway}")
    public ResponseEntity<String> handlePaymentCallback(
            @PathVariable String gateway,
            @RequestBody String callbackData) {
        try {
            boolean isValid = paymentService.processPaymentCallback(gateway, callbackData);
            if (isValid) {
                return ResponseEntity.ok("Callback xử lý thành công");
            } else {
                return ResponseEntity.badRequest().body("Dữ liệu callback không hợp lệ");
            }
        } catch (Exception e) {
            log.error("Lỗi khi xử lý callback thanh toán", e);
            return ResponseEntity.internalServerError().body("Lỗi khi xử lý callback");
        }
    }

    /**
     * Endpoint lấy danh sách các phương thức thanh toán hỗ trợ.
     *
     * @return {@link ResponseEntity} chứa danh sách các phương thức thanh toán.
     */
    @GetMapping("/methods")
    public ResponseEntity<List<String>> getSupportedPaymentMethods() {
        try {
            List<String> methods = paymentService.getSupportedPaymentMethods();
            return ResponseEntity.ok(methods);
        } catch (Exception e) {
            log.error("Lỗi khi lấy danh sách phương thức thanh toán", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Hàm trợ giúp tạo URL chuyển hướng tới frontend với kết quả thanh toán.
     *
     * @param response đối tượng {@link PaymentReturnResponse} chứa thông tin kết quả thanh toán.
     * @return URL chuyển hướng.
     */
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

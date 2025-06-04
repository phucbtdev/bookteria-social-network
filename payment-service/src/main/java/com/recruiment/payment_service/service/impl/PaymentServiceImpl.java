package com.recruiment.payment_service.service.impl;

import com.recruiment.payment_service.dto.PaymentRequest;
import com.recruiment.payment_service.dto.PaymentResponse;
import com.recruiment.payment_service.dto.PaymentReturnResponse;
import com.recruiment.payment_service.entity.Payment;
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
    private final List<PaymentGateway> paymentGateways;

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
                .map(this::mapToPaymentResponse)
                .collect(Collectors.toList());
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

    @Override
    @Transactional(readOnly = true)
    public List<String> getSupportedPaymentMethods() {
        return gatewayFactory.getSupportedGateways();
    }

    public PaymentReturnResponse processPaymentReturn(String gateway, Map<String, String[]> parameterMap) {
        try {
            log.info("Processing payment return for gateway: {}", gateway);

            // Convert parameter map to simple string map
            Map<String, String> params = new HashMap<>();
            for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                if (entry.getValue() != null && entry.getValue().length > 0) {
                    params.put(entry.getKey(), entry.getValue()[0]);
                }
            }

            // Process based on gateway type
            if ("VNPAY".equals(gateway)) {
                return processVNPayReturn(params);
            } else if ("MOMO".equals(gateway)) {
                return processMoMoReturn(params);
            } else {
                throw new IllegalArgumentException("Unsupported payment gateway: " + gateway);
            }

        } catch (Exception e) {
            log.error("Error processing payment return for gateway: {}", gateway, e);
            return PaymentReturnResponse.builder()
                    .success(false)
                    .status(Payment.PaymentStatus.FAILED)
                    .message("Payment processing failed: " + e.getMessage())
                    .build();
        }
    }

    private PaymentReturnResponse processVNPayReturn(Map<String, String> params) {
        try {
            String vnpTxnRef = params.get("vnp_TxnRef");
            String vnpResponseCode = params.get("vnp_ResponseCode");
            String vnpTransactionNo = params.get("vnp_TransactionNo");
            String vnpSecureHash = params.get("vnp_SecureHash");

            if (vnpTxnRef == null) {
                throw new IllegalArgumentException("Missing vnp_TxnRef parameter");
            }

            // Find payment by ID
            UUID paymentId = UUID.fromString(vnpTxnRef);
            Payment payment = paymentRepository.findById(paymentId)
                    .orElseThrow(() -> new RuntimeException("Payment not found: " + paymentId));

            // Verify signature (simplified - in production, implement proper signature verification)
            boolean isValidSignature = verifyVNPaySignature(params, vnpSecureHash);

            PaymentReturnResponse.PaymentReturnResponseBuilder responseBuilder = PaymentReturnResponse.builder()
                    .paymentId(paymentId)
                    .transactionId(vnpTransactionNo);

            if (isValidSignature && "00".equals(vnpResponseCode)) {
                // Payment successful
                payment.setStatus(Payment.PaymentStatus.SUCCESS);
                paymentRepository.save(payment);

                responseBuilder
                        .success(true)
                        .status(Payment.PaymentStatus.SUCCESS)
                        .message("Payment completed successfully");
            } else {
                // Payment failed
                payment.setStatus(Payment.PaymentStatus.FAILED);
                paymentRepository.save(payment);

                String errorMessage = getVNPayErrorMessage(vnpResponseCode);
                responseBuilder
                        .success(false)
                        .status(Payment.PaymentStatus.FAILED)
                        .message("Payment failed: " + errorMessage);
            }

            return responseBuilder.build();

        } catch (Exception e) {
            log.error("Error processing VNPay return", e);
            throw new RuntimeException("Error processing VNPay return", e);
        }
    }

    private PaymentReturnResponse processMoMoReturn(Map<String, String> params) {
        try {
            String orderId = params.get("orderId");
            String resultCode = params.get("resultCode");
            String transId = params.get("transId");
            String signature = params.get("signature");

            if (orderId == null) {
                throw new IllegalArgumentException("Missing orderId parameter");
            }

            // Find payment by ID
            UUID paymentId = UUID.fromString(orderId);
            Payment payment = paymentRepository.findById(paymentId)
                    .orElseThrow(() -> new RuntimeException("Payment not found: " + paymentId));

            // Verify signature (simplified - in production, implement proper signature verification)
            boolean isValidSignature = verifyMoMoSignature(params, signature);

            PaymentReturnResponse.PaymentReturnResponseBuilder responseBuilder = PaymentReturnResponse.builder()
                    .paymentId(paymentId)
                    .transactionId(transId);

            if (isValidSignature && "0".equals(resultCode)) {
                // Payment successful
                payment.setStatus(Payment.PaymentStatus.SUCCESS);
                paymentRepository.save(payment);

                responseBuilder
                        .success(true)
                        .status(Payment.PaymentStatus.SUCCESS)
                        .message("Payment completed successfully");
            } else {
                // Payment failed
                payment.setStatus(Payment.PaymentStatus.FAILED);
                paymentRepository.save(payment);

                String errorMessage = getMoMoErrorMessage(resultCode);
                responseBuilder
                        .success(false)
                        .status(Payment.PaymentStatus.FAILED)
                        .message("Payment failed: " + errorMessage);
            }

            return responseBuilder.build();

        } catch (Exception e) {
            log.error("Error processing MoMo return", e);
            throw new RuntimeException("Error processing MoMo return", e);
        }
    }

    private boolean verifyVNPaySignature(Map<String, String> params, String vnpSecureHash) {
        try {
            // Get VNPay gateway for signature verification
            VNPayGateway vnPayGateway = getPaymentGateway("VNPAY");
            if (vnPayGateway != null) {
                // In a real implementation, you would reconstruct the hash data
                // and verify against the provided secure hash
                // For now, return true as a placeholder
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("Error verifying VNPay signature", e);
            return false;
        }
    }

    private boolean verifyMoMoSignature(Map<String, String> params, String signature) {
        try {
            // Get MoMo gateway for signature verification
            MoMoGateway moMoGateway = getPaymentGateway("MOMO");
            if (moMoGateway != null) {
                // In a real implementation, you would reconstruct the signature
                // and verify against the provided signature
                // For now, return true as a placeholder
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("Error verifying MoMo signature", e);
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends PaymentGateway> T getPaymentGateway(String gatewayName) {
        return (T) paymentGateways.stream()
                .filter(gateway -> gatewayName.equals(gateway.getGatewayName()))
                .findFirst()
                .orElse(null);
    }

    private String getVNPayErrorMessage(String responseCode) {
        switch (responseCode) {
            case "00": return "Success";
            case "07": return "Trừ tiền thành công. Giao dịch bị nghi ngờ (liên quan tới lừa đảo, giao dịch bất thường).";
            case "09": return "Giao dịch không thành công do: Thẻ/Tài khoản của khách hàng chưa đăng ký dịch vụ InternetBanking tại ngân hàng.";
            case "10": return "Giao dịch không thành công do: Khách hàng xác thực thông tin thẻ/tài khoản không đúng quá 3 lần";
            case "11": return "Giao dịch không thành công do: Đã hết hạn chờ thanh toán. Xin quý khách vui lòng thực hiện lại giao dịch.";
            case "12": return "Giao dịch không thành công do: Thẻ/Tài khoản của khách hàng bị khóa.";
            case "13": return "Giao dịch không thành công do Quý khách nhập sai mật khẩu xác thực giao dịch (OTP).";
            case "24": return "Giao dịch không thành công do: Khách hàng hủy giao dịch";
            case "51": return "Giao dịch không thành công do: Tài khoản của quý khách không đủ số dư để thực hiện giao dịch.";
            case "65": return "Giao dịch không thành công do: Tài khoản của Quý khách đã vượt quá hạn mức giao dịch trong ngày.";
            case "75": return "Ngân hàng thanh toán đang bảo trì.";
            case "79": return "Giao dịch không thành công do: KH nhập sai mật khẩu thanh toán quá số lần quy định.";
            default: return "Giao dịch thất bại";
        }
    }

    private String getMoMoErrorMessage(String resultCode) {
        switch (resultCode) {
            case "0": return "Success";
            case "9000": return "Giao dịch được khởi tạo, chờ người dùng xác nhận thanh toán";
            case "8000": return "Giao dịch đang được xử lý";
            case "7000": return "Giao dịch bị từ chối bởi người dùng";
            case "6002": return "Giao dịch thất bại do lỗi từ phía MoMo";
            case "6001": return "Giao dịch thất bại do lỗi từ phía đối tác";
            case "3001": return "Không tìm thấy giao dịch";
            case "3002": return "Giao dịch không hợp lệ";
            case "2001": return "Số dư không đủ để thanh toán";
            case "1006": return "Giao dịch bị từ chối do vi phạm chính sách thanh toán của MoMo";
            case "1005": return "URL không hợp lệ";
            case "1004": return "Dữ liệu gửi lên không đúng định dạng";
            case "1001": return "Lỗi server";
            case "1000": return "Lỗi không xác định";
            default: return "Giao dịch thất bại";
        }
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

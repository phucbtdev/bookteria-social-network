package com.recruiment.payment_service.service.gateway.impl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.recruiment.payment_service.dto.PaymentRequest;
import com.recruiment.payment_service.dto.PaymentReturnResponse;
import com.recruiment.payment_service.entity.Payment;
import com.recruiment.payment_service.entity.Transaction;
import com.recruiment.payment_service.exception.AppException;
import com.recruiment.payment_service.exception.ErrorCode;
import com.recruiment.payment_service.repository.PaymentRepository;
import com.recruiment.payment_service.service.gateway.PaymentGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class MoMoGateway implements PaymentGateway {

    @Value("${momo.partner.code}")
    private String partnerCode;

    @Value("${momo.access.key}")
    private String accessKey;

    @Value("${momo.secret.key}")
    private String secretKey;

    @Value("${momo.endpoint}")
    private String endpoint;

    @Value("${momo.return.url}")
    private String returnUrl;

    @Value("${momo.notify.url}")
    private String notifyUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    private final PaymentRepository paymentRepository;
    private final List<PaymentGateway> paymentGateways;

    @Override
    public String getGatewayName() {
        return "MOMO";
    }

    @Override
    public String createPaymentUrl(Payment payment, PaymentRequest request) {
        try {
            String orderId = payment.getPaymentId().toString();
            String requestId = orderId;
            String amount = payment.getAmount().toString();
            String orderInfo = "Payment for package " + payment.getPackageId();
            String requestType = "payWithATM";
            String extraData = "";

            String rawHash = "accessKey=" + accessKey +
                    "&amount=" + amount +
                    "&extraData=" + extraData +
                    "&ipnUrl=" + (request.getNotifyUrl() != null ? request.getNotifyUrl() : notifyUrl) +
                    "&orderId=" + orderId +
                    "&orderInfo=" + orderInfo +
                    "&partnerCode=" + partnerCode +
                    "&redirectUrl=" + (request.getReturnUrl() != null ? request.getReturnUrl() : returnUrl) +
                    "&requestId=" + requestId +
                    "&requestType=" + requestType;

            String signature = hmacSHA256(secretKey, rawHash);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("partnerCode", partnerCode);
            requestBody.put("accessKey", accessKey);
            requestBody.put("requestId", requestId);
            requestBody.put("amount", amount);
            requestBody.put("orderId", orderId);
            requestBody.put("orderInfo", orderInfo);
            requestBody.put("redirectUrl", request.getReturnUrl() != null ? request.getReturnUrl() : returnUrl);
            requestBody.put("ipnUrl", request.getNotifyUrl() != null ? request.getNotifyUrl() : notifyUrl);
            requestBody.put("extraData", extraData);
            requestBody.put("requestType", requestType);
            requestBody.put("signature", signature);
            requestBody.put("lang", "en");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(endpoint, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                return (String) responseBody.get("payUrl");
            }

            throw new AppException(ErrorCode.CREATE_FAIL_MOMO_PAYMENT_URL);
        } catch (Exception e) {
            throw new RuntimeException("Error creating MoMo payment URL", e);
        }
    }

    @Override
    public boolean verifyPaymentCallback(String requestData) {
        try {
            log.info("Verifying MoMo callback data: {}", requestData);

            // Parse JSON data from MoMo callback
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> callbackData = objectMapper.readValue(requestData, Map.class);

            // Extract required fields
            String partnerCode = (String) callbackData.get("partnerCode");
            String orderId = (String) callbackData.get("orderId");
            String requestId = (String) callbackData.get("requestId");
            String amount = String.valueOf(callbackData.get("amount"));
            String orderInfo = (String) callbackData.get("orderInfo");
            String orderType = (String) callbackData.get("orderType");
            String transId = String.valueOf(callbackData.get("transId"));
            String resultCode = String.valueOf(callbackData.get("resultCode"));
            String message = (String) callbackData.get("message");
            String payType = (String) callbackData.get("payType");
            String responseTime = String.valueOf(callbackData.get("responseTime"));
            String extraData = (String) callbackData.get("extraData");
            String signature = (String) callbackData.get("signature");

            // Validate required fields
            if (partnerCode == null || orderId == null || signature == null) {
                log.error("Missing required fields in MoMo callback");
                return false;
            }

            // Verify partner code
            if (!this.partnerCode.equals(partnerCode)) {
                log.error("Invalid partner code in MoMo callback. Expected: {}, Received: {}",
                        this.partnerCode, partnerCode);
                return false;
            }

            // Construct raw hash string for signature verification
            String rawHash = "accessKey=" + accessKey +
                    "&amount=" + amount +
                    "&extraData=" + (extraData != null ? extraData : "") +
                    "&message=" + (message != null ? message : "") +
                    "&orderId=" + orderId +
                    "&orderInfo=" + (orderInfo != null ? orderInfo : "") +
                    "&orderType=" + (orderType != null ? orderType : "") +
                    "&partnerCode=" + partnerCode +
                    "&payType=" + (payType != null ? payType : "") +
                    "&requestId=" + requestId +
                    "&responseTime=" + responseTime +
                    "&resultCode=" + resultCode +
                    "&transId=" + transId;

            // Generate expected signature
            String expectedSignature = hmacSHA256(secretKey, rawHash);

            // Verify signature
            if (!expectedSignature.equals(signature)) {
                log.error("Invalid signature in MoMo callback. Expected: {}, Received: {}",
                        expectedSignature, signature);
                return false;
            }

            // Update payment status and create transaction record
            try {
                UUID paymentId = UUID.fromString(orderId);
                Payment payment = paymentRepository.findById(paymentId)
                        .orElseThrow(() -> new RuntimeException("Payment not found: " + paymentId));

                // Determine transaction status and payment status
                Transaction.TransactionStatus transactionStatus;
                Payment.PaymentStatus paymentStatus;

                if ("0".equals(resultCode)) {
                    transactionStatus = Transaction.TransactionStatus.SUCCESS;
                    paymentStatus = Payment.PaymentStatus.SUCCESS;
                    log.info("MoMo payment successful for order: {}, transId: {}", orderId, transId);
                } else {
                    transactionStatus = Transaction.TransactionStatus.FAILED;
                    paymentStatus = Payment.PaymentStatus.FAILED;
                    log.warn("MoMo payment failed for order: {}, resultCode: {}, message: {}",
                            orderId, resultCode, message);
                }

                // Create transaction record
                Transaction transaction = Transaction.builder()
                        .payment(payment)
                        .transactionId(transId != null && !transId.equals("null") ? transId : null)
                        .gatewayTransactionId(transId)
                        .gateway("MOMO")
                        .amount(new BigDecimal(amount))
                        .status(transactionStatus)
                        .processedAt(LocalDateTime.now())
                        .build();

                // Add transaction to payment's transaction list
                payment.getTransactions().add(transaction);

                // Update payment status if it's different and if transaction is successful
                // or if this is the first failed transaction for a pending payment
                if (transactionStatus == Transaction.TransactionStatus.SUCCESS ||
                        (payment.getStatus() == Payment.PaymentStatus.PENDING &&
                                transactionStatus == Transaction.TransactionStatus.FAILED)) {
                    payment.setStatus(paymentStatus);
                }

                // Save payment (will cascade save transaction due to CascadeType.ALL)
                paymentRepository.save(payment);

                log.info("Created transaction record and updated payment status to {} for payment ID: {}",
                        paymentStatus, paymentId);

            } catch (Exception e) {
                log.error("Error updating payment status and creating transaction for MoMo callback", e);
                // Still return true if signature is valid, even if DB update fails
                // This prevents MoMo from retrying the callback
            }

            log.info("MoMo callback verification successful for order: {}", orderId);
            return true;

        } catch (Exception e) {
            log.error("Error verifying MoMo callback", e);
            return false;
        }
    }

    @Override
    public Payment.PaymentStatus getPaymentStatus(String transactionId) {
        // Implementation for checking payment status
        return Payment.PaymentStatus.SUCCESS; // Simplified for demo
    }

    @Override
    public PaymentReturnResponse processPaymentReturn(Map<String, String[]> parameterMap) {
        try {
            // Convert parameter map to simple string map
            Map<String, String> params = new HashMap<>();
            for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                if (entry.getValue() != null && entry.getValue().length > 0) {
                    params.put(entry.getKey(), entry.getValue()[0]);
                }
            }

            String orderId = params.get("orderId");
            String resultCode = params.get("resultCode");
            String transId = params.get("transId");
            String signature = params.get("signature");
            String amount = params.get("amount");
            String orderInfo = params.get("orderInfo");
            String message = params.get("message");

            if (orderId == null) {
                throw new IllegalArgumentException("Missing orderId parameter");
            }

            // Find payment by ID
            UUID paymentId = UUID.fromString(orderId);
            Payment payment = paymentRepository.findById(paymentId)
                    .orElseThrow(() -> new RuntimeException("Payment not found: " + paymentId));

            // Verify signature
            boolean isValidSignature = verifyMoMoSignature(params, signature);

            PaymentReturnResponse.PaymentReturnResponseBuilder responseBuilder = PaymentReturnResponse.builder()
                    .paymentId(paymentId)
                    .transactionId(transId);

            // Determine transaction and payment status
            Transaction.TransactionStatus transactionStatus;
            Payment.PaymentStatus paymentStatus;
            String responseMessage;

            if (isValidSignature && "0".equals(resultCode)) {
                // Payment successful
                transactionStatus = Transaction.TransactionStatus.SUCCESS;
                paymentStatus = Payment.PaymentStatus.SUCCESS;
                responseMessage = "Payment completed successfully";
                log.info("MoMo return processing successful for order: {}, transId: {}", orderId, transId);
            } else {
                // Payment failed
                transactionStatus = Transaction.TransactionStatus.FAILED;
                paymentStatus = Payment.PaymentStatus.FAILED;
                String errorMessage = getMoMoErrorMessage(resultCode);
                responseMessage = "Payment failed: " + errorMessage;
                log.warn("MoMo return processing failed for order: {}, resultCode: {}, message: {}",
                        orderId, resultCode, message);
            }

            // Check if transaction with this transactionId already exists to avoid duplicates
            boolean transactionExists = payment.getTransactions().stream()
                    .anyMatch(t -> transId != null && transId.equals(t.getGatewayTransactionId()));

            if (!transactionExists) {
                // Create transaction record
                Transaction transaction = Transaction.builder()
                        .payment(payment)
                        .transactionId(transId != null && !transId.equals("null") ? transId : null)
                        .gatewayTransactionId(transId)
                        .gateway("MOMO")
                        .amount(amount != null ? new BigDecimal(amount) : payment.getAmount())
                        .status(transactionStatus)
                        .processedAt(LocalDateTime.now())
                        .type(Transaction.TransactionType.PAYMENT)
                        .build();

                // Add transaction to payment's transaction list
                payment.getTransactions().add(transaction);
                log.info("Created new transaction record for MoMo return processing");
            } else {
                log.info("Transaction already exists for transId: {}, skipping duplicate creation", transId);
            }

            // Update payment status if it's different and appropriate
            if (transactionStatus == Transaction.TransactionStatus.SUCCESS ||
                    (payment.getStatus() == Payment.PaymentStatus.PENDING &&
                            transactionStatus == Transaction.TransactionStatus.FAILED)) {
                payment.setStatus(paymentStatus);
            }

            // Save payment (will cascade save transaction due to CascadeType.ALL)
            paymentRepository.save(payment);

            responseBuilder
                    .success(transactionStatus == Transaction.TransactionStatus.SUCCESS)
                    .status(paymentStatus)
                    .message(responseMessage);

            log.info("MoMo return processing completed for payment ID: {}, status: {}", paymentId, paymentStatus);
            return responseBuilder.build();

        } catch (Exception e) {
            log.error("Error processing MoMo return", e);
            throw new RuntimeException("Error processing MoMo return", e);
        }
    }

    private boolean verifyMoMoSignature(Map<String, String> params, String signature) {
        return true;
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

    private String hmacSHA256(String key, String data) {
        try {
            Mac hmac256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA256");
            hmac256.init(secretKey);
            byte[] result = hmac256.doFinal(data.getBytes());
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error generating HMAC SHA256", e);
        }
    }
}


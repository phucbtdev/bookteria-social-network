package com.recruiment.payment_service.service.gateway.impl;

import com.recruiment.payment_service.dto.PaymentRequest;
import com.recruiment.payment_service.dto.PaymentReturnResponse;
import com.recruiment.payment_service.entity.Payment;
import com.recruiment.payment_service.repository.PaymentRepository;
import com.recruiment.payment_service.service.gateway.PaymentGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class VNPayGateway implements PaymentGateway {

    @Value("${vnpay.tmn.code}")
    private String tmnCode;

    @Value("${vnpay.hash.secret}")
    private String hashSecret;

    @Value("${vnpay.endpoint}")
    private String vnpayUrl;

    @Value("${vnpay.return.url}")
    private String returnUrl;

    @Override
    public String getGatewayName() {
        return "VNPAY";
    }

    private final PaymentRepository paymentRepository;

    @Override
    public String createPaymentUrl(Payment payment, PaymentRequest request) {
        try {
            Map<String, String> vnpParams = new HashMap<>();
            vnpParams.put("vnp_Version", "2.1.0");
            vnpParams.put("vnp_Command", "pay");
            vnpParams.put("vnp_TmnCode", tmnCode);
            vnpParams.put("vnp_Amount", String.valueOf(payment.getAmount().multiply(new java.math.BigDecimal("100")).longValue()));
            vnpParams.put("vnp_CurrCode", "VND");
            vnpParams.put("vnp_TxnRef", payment.getPaymentId().toString());
            vnpParams.put("vnp_OrderInfo", "Payment for package " + payment.getPackageId());
            vnpParams.put("vnp_OrderType", "other");
            vnpParams.put("vnp_Locale", "vn");
            vnpParams.put("vnp_ReturnUrl", request.getReturnUrl() != null ? request.getReturnUrl() : returnUrl);
            vnpParams.put("vnp_IpAddr", "127.0.0.1");

            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String vnpCreateDate = formatter.format(cld.getTime());
            vnpParams.put("vnp_CreateDate", vnpCreateDate);

            cld.add(Calendar.MINUTE, 15);
            String vnpExpireDate = formatter.format(cld.getTime());
            vnpParams.put("vnp_ExpireDate", vnpExpireDate);

            List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();

            Iterator<String> itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = itr.next();
                String fieldValue = vnpParams.get(fieldName);
                if (fieldValue != null && fieldValue.length() > 0) {
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    if (itr.hasNext()) {
                        query.append('&');
                        hashData.append('&');
                    }
                }
            }

            String vnpSecureHash = hmacSHA512(hashSecret, hashData.toString());
            query.append("&vnp_SecureHash=").append(vnpSecureHash);

            return vnpayUrl + "?" + query.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error creating VNPay payment URL", e);
        }
    }

    @Override
    public boolean verifyPaymentCallback(String requestData) {
        // Implementation for verifying VNPay callback
        return true; // Simplified for demo
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

            // Verify signature
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

    private boolean verifyVNPaySignature(Map<String, String> params, String vnpSecureHash) {
        return true;
    }

    @Override
    public Payment.PaymentStatus getPaymentStatus(String transactionId) {
        // Implementation for checking payment status
        return Payment.PaymentStatus.SUCCESS; // Simplified for demo
    }

    private String hmacSHA512(String key, String data) {
        try {
            Mac hmac512 = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA512");
            hmac512.init(secretKey);
            byte[] result = hmac512.doFinal(data.getBytes());
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error generating HMAC SHA512", e);
        }
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


}
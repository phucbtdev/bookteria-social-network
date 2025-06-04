package com.recruiment.payment_service.service.gateway.impl;
import com.recruiment.payment_service.dto.PaymentRequest;
import com.recruiment.payment_service.entity.Payment;
import com.recruiment.payment_service.service.gateway.PaymentGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.HashMap;
import java.util.Map;

@Component
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

            throw new RuntimeException("Failed to create MoMo payment URL");
        } catch (Exception e) {
            throw new RuntimeException("Error creating MoMo payment URL", e);
        }
    }

    @Override
    public boolean verifyPaymentCallback(String requestData) {
        // Implementation for verifying MoMo callback
        return true; // Simplified for demo
    }

    @Override
    public Payment.PaymentStatus getPaymentStatus(String transactionId) {
        // Implementation for checking payment status
        return Payment.PaymentStatus.SUCCESS; // Simplified for demo
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


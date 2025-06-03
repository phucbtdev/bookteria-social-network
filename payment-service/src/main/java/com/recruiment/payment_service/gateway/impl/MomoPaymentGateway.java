package com.recruiment.payment_service.gateway.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recruiment.payment_service.dto.PaymentRequest;
import com.recruiment.payment_service.dto.PaymentResponse;
import com.recruiment.payment_service.exception.AppException;
import com.recruiment.payment_service.exception.ErrorCode;
import com.recruiment.payment_service.gateway.PaymentGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Cổng thanh toán Momo - implement PaymentGateway
 * Dùng java.util.HexFormat thay thế Apache Commons Codec
 */
@Component("momoPaymentGateway")
@Slf4j
public class MomoPaymentGateway implements PaymentGateway {

    private static final String HMAC_SHA256 = "HmacSHA256";

    @Value("${momo.endpoint.create-payment}")
    private String momoCreatePaymentUrl;

    @Value("${momo.endpoint.query-payment}")
    private String momoQueryPaymentUrl;

    @Value("${momo.partner.code}")
    private String partnerCode;

    @Value("${momo.access.key}")
    private String accessKey;

    @Value("${momo.secret.key}")
    private String secretKey;

    @Value("${momo.return.url}")
    private String returnUrl;

    @Value("${momo.notify.url}")
    private String notifyUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public PaymentResponse createPayment(PaymentRequest request) {
        try {
            String orderId = "MOMO_" + UUID.randomUUID().toString();
            String requestId = UUID.randomUUID().toString();
            String amount = request.getAmount().toBigInteger().toString();
            String orderInfo = "Thanh toán gói " + request.getPackageId() + " cho user " + request.getUserId();
            String extraData = "";

            String rawSignature = "accessKey=" + accessKey +
                    "&amount=" + amount +
                    "&extraData=" + extraData +
                    "&ipnUrl=" + notifyUrl +
                    "&orderId=" + orderId +
                    "&orderInfo=" + orderInfo +
                    "&partnerCode=" + partnerCode +
                    "&redirectUrl=" + returnUrl +
                    "&requestId=" + requestId +
                    "&requestType=captureWallet";

            String signature = hmacSHA256(rawSignature, secretKey);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("partnerCode", partnerCode);
            requestBody.put("accessKey", accessKey);
            requestBody.put("requestId", requestId);
            requestBody.put("amount", amount);
            requestBody.put("orderId", orderId);
            requestBody.put("orderInfo", orderInfo);
            requestBody.put("redirectUrl", returnUrl);
            requestBody.put("ipnUrl", notifyUrl);
            requestBody.put("extraData", extraData);
            requestBody.put("requestType", "captureWallet");
            requestBody.put("signature", signature);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.postForEntity(momoCreatePaymentUrl, entity, String.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK && responseEntity.getBody() != null) {
                Map<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), Map.class);
                String payUrl = (String) responseMap.get("payUrl");
                String momoPayId = (String) responseMap.get("orderId");

                log.info("Momo payment created: payUrl={}, orderId={}", payUrl, momoPayId);

                return new PaymentResponse(momoPayId, payUrl, "PENDING", "Momo payment created successfully");
            } else {
                log.error("Momo payment failed: response = {}", responseEntity);
                return new PaymentResponse(null, null, "FAILED", "Momo payment creation failed");
            }
        } catch (Exception e) {
            log.error("Exception when creating Momo payment", e);
            return new PaymentResponse(null, null, "FAILED", "Error when creating payment");
        }
    }

    @Override
    public boolean verifyPaymentCallback(String params) {
        try {
            if (!StringUtils.hasText(params)) {
                log.warn("Empty callback params");
                return false;
            }

            Map<String, Object> map = objectMapper.readValue(params, Map.class);
            String signature = (String) map.get("signature");

            String rawSignature = "accessKey=" + map.get("accessKey") +
                    "&amount=" + map.get("amount") +
                    "&extraData=" + map.get("extraData") +
                    "&message=" + map.get("message") +
                    "&orderId=" + map.get("orderId") +
                    "&orderInfo=" + map.get("orderInfo") +
                    "&orderType=" + map.get("orderType") +
                    "&partnerCode=" + map.get("partnerCode") +
                    "&payType=" + map.get("payType") +
                    "&requestId=" + map.get("requestId") +
                    "&responseTime=" + map.get("responseTime") +
                    "&resultCode=" + map.get("resultCode") +
                    "&transId=" + map.get("transId");

            String computedSignature = hmacSHA256(rawSignature, secretKey);

            boolean valid = signature != null && signature.equals(computedSignature);

            if (valid) {
                log.info("Momo callback signature valid for orderId={}", map.get("orderId"));
            } else {
                log.warn("Momo callback signature invalid");
            }
            return valid;

        } catch (Exception e) {
            log.error("Exception verifying Momo callback", e);
            return false;
        }
    }

    private String hmacSHA256(String data, String key) {
        try {
            Mac mac = Mac.getInstance(HMAC_SHA256);
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), HMAC_SHA256);
            mac.init(secretKeySpec);
            byte[] hmacData = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return java.util.HexFormat.of().formatHex(hmacData);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new AppException(ErrorCode.GENERATE_FAILED);
        }
    }
}


package com.recruiment.payment_service.gateway.impl;

import com.recruiment.payment_service.dto.PaymentRequest;
import com.recruiment.payment_service.dto.PaymentResponse;
import com.recruiment.payment_service.exception.AppException;
import com.recruiment.payment_service.exception.ErrorCode;
import com.recruiment.payment_service.gateway.PaymentGateway;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
 * Cổng thanh toán VNPAY - Implement PaymentGateway
 * Ví dụ mô phỏng gọi API tạo đơn và xác thực callback
 */
@Component("vnpayPaymentGateway")
@Slf4j
public class VnpayPaymentGateway implements PaymentGateway {

    @Value("${vnpay.endpoint.create-payment}")
    private String vnpayCreatePaymentUrl;

    @Value("${vnpay.return.url}")
    private String returnUrl;

    @Value("${vnpay.notify.url}")
    private String notifyUrl;

    @Value("${vnpay.tmn.code}")
    private String tmnCode;

    @Value("${vnpay.hash.secret}")
    private String hashSecret;

    private static final String HMAC_SHA256 = "HmacSHA256";

    @Override
    public PaymentResponse createPayment(PaymentRequest request) {
        try {
            String orderId = "VNPAY_" + UUID.randomUUID().toString();
            String amount = request.getAmount().toBigInteger().toString();

            // Tạo dữ liệu theo yêu cầu VNPAY
            Map<String, String> vnpParams = new HashMap<>();
            vnpParams.put("vnp_Version", "2.1.0");
            vnpParams.put("vnp_Command", "pay");
            vnpParams.put("vnp_TmnCode", tmnCode);
            vnpParams.put("vnp_Amount", String.valueOf(Long.parseLong(amount) * 100)); // VNPAY nhân 100
            vnpParams.put("vnp_CurrCode", "VND");
            vnpParams.put("vnp_TxnRef", orderId);
            vnpParams.put("vnp_OrderInfo", "Thanh toán gói " + request.getPackageId() + " cho user " + request.getUserId());
            vnpParams.put("vnp_OrderType", "200000");
            vnpParams.put("vnp_ReturnUrl", returnUrl);
            vnpParams.put("vnp_IpAddr", "127.0.0.1"); // Thực tế lấy IP client
            vnpParams.put("vnp_CreateDate", new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));

            // Sắp xếp tham số theo key alphabet
            SortedMap<String, String> sortedParams = new TreeMap<>(vnpParams);

            // Tạo chuỗi dữ liệu query string
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            Iterator<Map.Entry<String, String>> itr = sortedParams.entrySet().iterator();
            while (itr.hasNext()) {
                Map.Entry<String, String> entry = itr.next();
                hashData.append(entry.getKey()).append('=').append(entry.getValue());
                query.append(entry.getKey()).append('=').append(entry.getValue());
                if (itr.hasNext()) {
                    hashData.append('&');
                    query.append('&');
                }
            }

            // Tạo chữ ký SHA256 (hmacSHA256)
            String secureHash = hmacSHA256(hashData.toString(), hashSecret);
            query.append("&vnp_SecureHash=").append(secureHash);

            String paymentUrl = vnpayCreatePaymentUrl + "?" + query.toString();

            log.info("VNPAY payment created: paymentUrl={}, orderId={}", paymentUrl, orderId);

            return new PaymentResponse(orderId, paymentUrl, "PENDING", "VNPAY payment created successfully");

        } catch (Exception e) {
            log.error("Exception when creating VNPAY payment", e);
            return new PaymentResponse(null, null, "FAILED", "Error when creating VNPAY payment");
        }
    }

    @Override
    public boolean verifyPaymentCallback(String params) {
        try {
            if (!StringUtils.hasText(params)) {
                log.warn("Empty callback params");
                return false;
            }

            // Giả sử params là query string callback của VNPAY, ví dụ:
            // vnp_Amount=1000000&vnp_BankCode=NCB&...&vnp_SecureHash=xxxx
            // Bạn cần tách params thành map key-value, lấy vnp_SecureHash ra, tạo lại hash từ phần còn lại để so sánh

            Map<String, String> queryMap = splitQuery(params);
            String vnpSecureHash = queryMap.remove("vnp_SecureHash");
            String vnpSecureHashType = queryMap.remove("vnp_SecureHashType");

            // Sắp xếp map theo key alphabet
            SortedMap<String, String> sortedParams = new TreeMap<>(queryMap);

            // Tạo chuỗi hashData
            StringBuilder hashData = new StringBuilder();
            Iterator<Map.Entry<String, String>> itr = sortedParams.entrySet().iterator();
            while (itr.hasNext()) {
                Map.Entry<String, String> entry = itr.next();
                hashData.append(entry.getKey()).append('=').append(entry.getValue());
                if (itr.hasNext()) {
                    hashData.append('&');
                }
            }

            // Tạo hash từ dữ liệu
            String calculatedHash = hmacSHA256(hashData.toString(), hashSecret);

            boolean valid = vnpSecureHash != null && vnpSecureHash.equalsIgnoreCase(calculatedHash);
            if (valid) {
                log.info("VNPAY callback signature valid for txnRef={}", queryMap.get("vnp_TxnRef"));
            } else {
                log.warn("VNPAY callback signature invalid");
            }
            return valid;

        } catch (Exception e) {
            log.error("Exception verifying VNPAY callback", e);
            return false;
        }
    }

    /**
     * Hàm chuyển đổi chuỗi query params thành Map
     */
    private Map<String, String> splitQuery(String query) {
        Map<String, String> queryPairs = new HashMap<>();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            if (idx > 0 && pair.length() > idx + 1) {
                String key = java.net.URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8);
                String value = java.net.URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8);
                queryPairs.put(key, value);
            }
        }
        return queryPairs;
    }

    /**
     * Hàm tạo chữ ký HMAC SHA256
     */

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

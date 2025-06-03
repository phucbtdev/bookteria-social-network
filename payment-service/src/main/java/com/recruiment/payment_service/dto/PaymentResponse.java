package com.recruiment.payment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Đối tượng trả về thông tin thanh toán (ví dụ link thanh toán, paymentId, trạng thái,...)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private String paymentId;
    private String paymentUrl; // link để redirect khách thanh toán
    private String status; // ví dụ: PENDING
    private String message; // Thông báo thêm nếu có
}

package com.recruiment.payment_service.service;

import com.recruiment.payment_service.dto.PaymentRequest;
import com.recruiment.payment_service.dto.PaymentResponse;
import com.recruiment.payment_service.dto.PaymentReturnResponse;
import com.recruiment.payment_service.entity.Payment;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Interface cung cấp các phương thức liên quan đến việc xử lý thanh toán.
 * Bao gồm các phương thức để tạo thanh toán, lấy thông tin thanh toán,
 * cập nhật trạng thái thanh toán, xử lý callback từ các cổng thanh toán và lấy các phương thức thanh toán hỗ trợ.
 */
public interface PaymentService {

    /**
     * Tạo mới một giao dịch thanh toán.
     *
     * @param request đối tượng {@link PaymentRequest} chứa thông tin thanh toán.
     * @return {@link PaymentResponse} chứa kết quả thanh toán.
     */
    PaymentResponse createPayment(PaymentRequest request);

    /**
     * Lấy thông tin thanh toán theo ID thanh toán.
     *
     * @param paymentId ID của thanh toán cần lấy thông tin.
     * @return {@link PaymentResponse} chứa thông tin thanh toán.
     */
    PaymentResponse getPaymentById(UUID paymentId);

    /**
     * Lấy danh sách các thanh toán của một người dùng.
     *
     * @param userId   ID của người dùng cần lấy thanh toán.
     * @param userType Loại người dùng (ví dụ: khách hàng, admin).
     * @return danh sách các {@link PaymentResponse} chứa thông tin thanh toán.
     */
    List<PaymentResponse> getPaymentsByUser(UUID userId, Payment.UserType userType);

    /**
     * Cập nhật trạng thái thanh toán.
     *
     * @param paymentId ID của thanh toán cần cập nhật trạng thái.
     * @param status    Trạng thái mới của thanh toán.
     * @return {@link PaymentResponse} chứa kết quả cập nhật trạng thái thanh toán.
     */
    PaymentResponse updatePaymentStatus(UUID paymentId, Payment.PaymentStatus status);

    /**
     * Xử lý callback trả về từ cổng thanh toán.
     *
     * @param gateway    tên của cổng thanh toán (ví dụ: VNPay, MoMo).
     * @param callbackData dữ liệu callback từ cổng thanh toán.
     * @return true nếu callback hợp lệ, false nếu không hợp lệ.
     */
    boolean processPaymentCallback(String gateway, String callbackData);

    /**
     * Lấy danh sách các phương thức thanh toán hỗ trợ.
     *
     * @return danh sách các phương thức thanh toán hỗ trợ.
     */
    List<String> getSupportedPaymentMethods();

    /**
     * Xử lý callback trả về từ cổng thanh toán và tạo {@link PaymentReturnResponse}.
     *
     * @param gateway        tên của cổng thanh toán.
     * @param parameterMap   map chứa các tham số trả về từ cổng thanh toán.
     * @return {@link PaymentReturnResponse} chứa kết quả xử lý callback.
     */
    PaymentReturnResponse processPaymentReturn(String gateway, Map<String, String[]> parameterMap);
}

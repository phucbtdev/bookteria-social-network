package com.recruiment.payment_service.repository;

import com.recruiment.payment_service.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    // Tìm transaction theo gatewayTransactionId
    Optional<Transaction> findByGatewayTransactionId(String gatewayTransactionId);

    // Tìm transaction theo gatewayOrderId
    Optional<Transaction> findByGatewayOrderId(String gatewayOrderId);

    // Tìm transactions theo payment
    List<Transaction> findByPaymentPaymentId(UUID paymentId);

    // Tìm transactions theo gateway
    List<Transaction> findByGateway(String gateway);

    // Tìm transactions theo status
    List<Transaction> findByStatus(Transaction.TransactionStatus status);

    // Tìm transactions theo payment và status
    List<Transaction> findByPaymentPaymentIdAndStatus(UUID paymentId, Transaction.TransactionStatus status);

    // Tìm successful transaction của payment
    @Query("SELECT t FROM Transaction t WHERE t.payment.paymentId = :paymentId AND t.status = 'SUCCESS' ORDER BY t.processedAt DESC")
    Optional<Transaction> findSuccessfulTransactionByPaymentId(@Param("paymentId") UUID paymentId);

    // Tìm latest transaction của payment
    @Query("SELECT t FROM Transaction t WHERE t.payment.paymentId = :paymentId ORDER BY t.createdAt DESC")
    List<Transaction> findLatestTransactionsByPaymentId(@Param("paymentId") UUID paymentId);

    // Tìm transactions trong khoảng thời gian
    @Query("SELECT t FROM Transaction t WHERE t.createdAt BETWEEN :startDate AND :endDate")
    List<Transaction> findTransactionsBetweenDates(@Param("startDate") LocalDateTime startDate,
                                                   @Param("endDate") LocalDateTime endDate);

    // Tìm transactions theo gateway và response code
    List<Transaction> findByGatewayAndGatewayResponseCode(String gateway, String responseCode);

    // Đếm transactions theo status
    long countByStatus(Transaction.TransactionStatus status);

    // Đếm transactions theo gateway
    long countByGateway(String gateway);

    // Tìm transactions cần xử lý (pending)
    List<Transaction> findByStatusAndCreatedAtBefore(Transaction.TransactionStatus status, LocalDateTime before);

    // Kiểm tra transaction đã tồn tại
    boolean existsByGatewayTransactionId(String gatewayTransactionId);

    // Tìm transactions duplicate (cùng gateway transaction id)
    @Query("SELECT t FROM Transaction t WHERE t.gatewayTransactionId = :gatewayTransactionId AND t.transactionId != :currentTransactionId")
    List<Transaction> findDuplicateTransactions(@Param("gatewayTransactionId") String gatewayTransactionId,
                                                @Param("currentTransactionId") UUID currentTransactionId);
}
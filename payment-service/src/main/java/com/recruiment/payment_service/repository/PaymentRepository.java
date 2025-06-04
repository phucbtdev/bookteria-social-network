package com.recruiment.payment_service.repository;

import com.recruiment.payment_service.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    List<Payment> findByUserIdAndUserType(UUID userId, Payment.UserType userType);

    List<Payment> findByStatus(Payment.PaymentStatus status);

    @Query("SELECT p FROM Payment p WHERE p.userId = :userId AND p.userType = :userType AND p.status = :status")
    List<Payment> findByUserIdAndUserTypeAndStatus(
            @Param("userId") UUID userId,
            @Param("userType") Payment.UserType userType,
            @Param("status") Payment.PaymentStatus status
    );

    Optional<Payment> findByPaymentId(UUID paymentId);
}

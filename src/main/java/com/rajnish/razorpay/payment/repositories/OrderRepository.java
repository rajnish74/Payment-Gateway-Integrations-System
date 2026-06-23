package com.rajnish.razorpay.payment.repositories;

import com.rajnish.razorpay.payment.dto.response.OrderResponse;
import com.rajnish.razorpay.payment.entity.OrderRecord;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderRecord, UUID> {
    boolean existsByMerchantIdAndReceipt(UUID merchantId, String receipt);

   Optional<OrderRecord> findByIdAndMerchantId(UUID merchantId, UUID orderId);
}

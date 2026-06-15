package com.rajnish.razorpay.payment.dto.request;

import com.rajnish.razorpay.common.entity.Money;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

public record CreateOrderRequest(

        @NotNull(message = "Amount is required")
        Money amount,

        @Size(max = 100, message = "Receipt should not exceed 100 characters")
        String receipt,
        Map<String, Object> notes,
        LocalDateTime expiresAt
) {
}

package com.rajnish.razorpay.payment.processor.dto;

import com.rajnish.razorpay.common.entity.Money;
import com.rajnish.razorpay.common.enums.PaymentMethod;

import java.util.Map;

public record PaymentProcessorRequest(
        PaymentMethod method,
        Money amount,
        Map<String,Object> methodDetails
) {
}

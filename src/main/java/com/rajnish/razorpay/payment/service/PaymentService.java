package com.rajnish.razorpay.payment.service;

import com.rajnish.razorpay.common.enums.PaymentStatus;
import com.rajnish.razorpay.payment.dto.request.PaymentInitRequest;
import com.rajnish.razorpay.payment.dto.response.PaymentResponse;

import java.util.UUID;

public interface PaymentService {

    PaymentResponse initiate(UUID merchantId, PaymentInitRequest request);

    PaymentResponse capture(UUID merchantId, UUID paymentId);
}

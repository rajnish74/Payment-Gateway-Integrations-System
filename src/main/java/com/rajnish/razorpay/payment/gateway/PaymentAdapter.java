package com.rajnish.razorpay.payment.gateway;

import com.rajnish.razorpay.payment.gateway.dto.PaymentRequest;
import com.rajnish.razorpay.payment.gateway.dto.PaymentResult;

public interface PaymentAdapter {

    PaymentResult initiate(PaymentRequest request);
}

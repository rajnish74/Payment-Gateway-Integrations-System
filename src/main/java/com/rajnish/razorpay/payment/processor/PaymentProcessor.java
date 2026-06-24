package com.rajnish.razorpay.payment.processor;

import com.rajnish.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.rajnish.razorpay.payment.processor.dto.PaymentProcessorResponse;

public interface PaymentProcessor {

    PaymentProcessorResponse charge(PaymentProcessorRequest request);
}

package com.rajnish.razorpay.payment.processor.strategy;

import com.rajnish.razorpay.payment.processor.PaymentProcessor;
import com.rajnish.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.rajnish.razorpay.payment.processor.dto.PaymentProcessorResponse;

public class NetBankingPaymentProcessor implements PaymentProcessor {
    @Override
    public PaymentProcessorResponse charge(PaymentProcessorRequest request) {
        return null;
    }
}

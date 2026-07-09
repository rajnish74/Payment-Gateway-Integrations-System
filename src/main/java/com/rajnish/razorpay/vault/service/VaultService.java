package com.rajnish.razorpay.vault.service;

import com.rajnish.razorpay.common.entity.Money;
import com.rajnish.razorpay.payment.processor.dto.PaymentProcessorResponse;
import com.rajnish.razorpay.vault.dto.request.TokenizeRequest;
import com.rajnish.razorpay.vault.dto.response.TokenizeResponse;
import jakarta.validation.Valid;

import java.util.Map;
import java.util.UUID;

public interface VaultService {
    TokenizeResponse tokenize(TokenizeRequest request, UUID merchantId);

    PaymentProcessorResponse charge(UUID paymentId,String token, Money amount, Map<String, Object> methodDetails);
}

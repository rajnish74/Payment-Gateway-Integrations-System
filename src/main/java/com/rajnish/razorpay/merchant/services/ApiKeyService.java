package com.rajnish.razorpay.merchant.services;

import com.rajnish.razorpay.merchant.dto.request.CreateApiKeyRequest;
import com.rajnish.razorpay.merchant.dto.response.ApiKeyCreateResponse;

import java.util.UUID;

public interface ApiKeyService {
    ApiKeyCreateResponse createApiKey(UUID merchantId,CreateApiKeyRequest request);
}

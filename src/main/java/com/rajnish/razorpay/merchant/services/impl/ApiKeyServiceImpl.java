package com.rajnish.razorpay.merchant.services.impl;

import com.rajnish.razorpay.common.exceptions.ResourceNotFoundException;
import com.rajnish.razorpay.merchant.dto.request.CreateApiKeyRequest;
import com.rajnish.razorpay.merchant.dto.response.ApiKeyCreateResponse;
import com.rajnish.razorpay.merchant.entity.ApiKey;
import com.rajnish.razorpay.merchant.entity.Merchant;
import com.rajnish.razorpay.merchant.repository.ApiKeyRepository;
import com.rajnish.razorpay.merchant.repository.MerchantRepository;
import com.rajnish.razorpay.merchant.services.ApiKeyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
@RequiredArgsConstructor
@Slf4j
public class ApiKeyServiceImpl implements ApiKeyService {

    private final MerchantRepository merchantRepository;
    private final ApiKeyRepository apiKeyRepository;

    @Override
    public ApiKeyCreateResponse createApiKey(UUID merchantId, CreateApiKeyRequest request) {
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(()->new  ResourceNotFoundException("merchant", merchantId));

        String keyId="rzp_"+request.environment().name().toUpperCase()+"big...";
        String rawSecret="big random secret";

        ApiKey apiKey=ApiKey.builder()
                .merchant(merchant)
                .keyId(keyId)
                .keySecretHash(rawSecret)
                .environment(request.environment())
                .build();

        apiKey=apiKeyRepository.save(apiKey);

        return new ApiKeyCreateResponse(
                apiKey.getId(),
                keyId,
                rawSecret,
                request.environment()
        );
    }
}

package com.rajnish.razorpay.merchant.controller;

import com.rajnish.razorpay.merchant.dto.request.CreateApiKeyRequest;
import com.rajnish.razorpay.merchant.dto.response.ApiKeyCreateResponse;
import com.rajnish.razorpay.merchant.dto.response.ApiKeyResponse;
import com.rajnish.razorpay.merchant.services.ApiKeyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("v1/merchants/{merchantId}/api-keys")
@RequiredArgsConstructor
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    @PostMapping
    public ResponseEntity<ApiKeyCreateResponse> createApiKey(@PathVariable UUID merchantId, @Valid
                                                             @RequestBody CreateApiKeyRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(apiKeyService.createApiKey(merchantId, request));
    }

    @GetMapping
    public ResponseEntity<List<ApiKeyResponse>> getListMerchantApiKeys(@PathVariable UUID merchantId) {
        return ResponseEntity.ok(apiKeyService.getListMerchantApiKeys(merchantId));
    }

    @DeleteMapping("/keyId")
    public ResponseEntity<Void> deleteApiKey(@PathVariable UUID merchantId, @PathVariable UUID keyId) {
        apiKeyService.deleteApiKey(merchantId,keyId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("{keyId}/rotate")
    public ResponseEntity<ApiKeyCreateResponse> rotateApiKey(@PathVariable UUID merchantId, @PathVariable UUID keyId) {
        return ResponseEntity.ok(apiKeyService.rotateApiKey(merchantId,keyId));
    }

}

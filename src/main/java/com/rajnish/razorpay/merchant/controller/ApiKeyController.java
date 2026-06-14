package com.rajnish.razorpay.merchant.controller;

import com.rajnish.razorpay.merchant.dto.request.CreateApiKeyRequest;
import com.rajnish.razorpay.merchant.dto.response.ApiKeyCreateResponse;
import com.rajnish.razorpay.merchant.services.ApiKeyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("v1/merchants/{merchantId}/api-keys")
@RequiredArgsConstructor
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    @PostMapping("/{merchantId}")
    public ResponseEntity<ApiKeyCreateResponse> createApiKey(@PathVariable UUID merchantId, @Valid
                                                             @RequestBody CreateApiKeyRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(apiKeyService.createApiKey(merchantId, request));
    }

}

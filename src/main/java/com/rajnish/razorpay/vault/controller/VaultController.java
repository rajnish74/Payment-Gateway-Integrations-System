package com.rajnish.razorpay.vault.controller;

import com.rajnish.razorpay.merchant.security.MerchantContext;
import com.rajnish.razorpay.vault.dto.request.TokenizeRequest;
import com.rajnish.razorpay.vault.dto.response.TokenizeResponse;
import com.rajnish.razorpay.vault.service.VaultService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class VaultController {

    private final VaultService vaultService;
    private final MerchantContext  merchantContext;

    @PostMapping("/tokenize")
    public ResponseEntity<TokenizeResponse> tokenize(@Valid @RequestBody TokenizeRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(
                vaultService.tokenize(request,merchantContext.getMerchantId())
        );
    }

}

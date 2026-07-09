package com.rajnish.razorpay.vault.controller;

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
    UUID merchantId=UUID.fromString("5b511b20-9e05-4086-8d02-63e51b860289");

    @PostMapping("/tokenize")
    public ResponseEntity<TokenizeResponse> tokenize(@Valid @RequestBody TokenizeRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(
                vaultService.tokenize(request,merchantId)
        );
    }

}

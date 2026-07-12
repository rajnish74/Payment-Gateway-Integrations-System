package com.rajnish.razorpay.merchant.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotBlank(message = "Email is required")
        @Email
        String email,

        @NotBlank
        String password
) {
}

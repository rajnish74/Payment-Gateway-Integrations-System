package com.rajnish.razorpay.merchant.dto.request;

import com.rajnish.razorpay.common.enums.Environment;

public record CreateApiKeyRequest(
        Environment environment
) {
}

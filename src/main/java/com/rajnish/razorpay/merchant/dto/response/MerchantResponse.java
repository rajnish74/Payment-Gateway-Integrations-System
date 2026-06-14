package com.rajnish.razorpay.merchant.dto.response;

import com.rajnish.razorpay.common.enums.BusinessType;
import com.rajnish.razorpay.common.enums.MerchantStatus;

import java.util.UUID;

public record MerchantResponse(
        UUID id,
        String name,
        String email,
        String businessName,
        BusinessType businessType,
        MerchantStatus merchantStatus
) {
}

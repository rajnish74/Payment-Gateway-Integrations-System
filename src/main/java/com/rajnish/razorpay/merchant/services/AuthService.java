package com.rajnish.razorpay.merchant.services;

import com.rajnish.razorpay.merchant.dto.request.MerchantSignupRequest;
import com.rajnish.razorpay.merchant.dto.response.MerchantResponse;

public interface AuthService {
    MerchantResponse signup( MerchantSignupRequest request);
}

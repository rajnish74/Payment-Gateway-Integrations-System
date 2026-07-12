package com.rajnish.razorpay.merchant.services;

import com.rajnish.razorpay.merchant.dto.request.LoginRequest;
import com.rajnish.razorpay.merchant.dto.request.MerchantSignupRequest;
import com.rajnish.razorpay.merchant.dto.response.LoginResponse;
import com.rajnish.razorpay.merchant.dto.response.MerchantResponse;
import jakarta.validation.Valid;

public interface AuthService {
    MerchantResponse signup( MerchantSignupRequest request);

    LoginResponse login(LoginRequest request);
}

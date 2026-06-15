package com.rajnish.razorpay.payment.service;

import com.rajnish.razorpay.payment.dto.request.CreateOrderRequest;
import com.rajnish.razorpay.payment.dto.response.OrderResponse;

import java.util.UUID;

public interface OrderService {
    OrderResponse createOrder(UUID merchantId, CreateOrderRequest request);
}

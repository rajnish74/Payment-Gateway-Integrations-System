package com.rajnish.razorpay.payment.controller;

import com.rajnish.razorpay.merchant.security.MerchantContext;
import com.rajnish.razorpay.payment.dto.request.CreateOrderRequest;
import com.rajnish.razorpay.payment.dto.response.OrderResponse;
import com.rajnish.razorpay.payment.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/orders")
public class OrderController {

    private final OrderService orderService;
    private final MerchantContext merchantContext;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody @Valid CreateOrderRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.createOrder(merchantContext.getMerchantId(),request));
    }
}

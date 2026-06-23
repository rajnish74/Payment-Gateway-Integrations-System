package com.rajnish.razorpay.payment.service.impl;

import com.rajnish.razorpay.common.enums.OrderStatus;
import com.rajnish.razorpay.common.exceptions.BusinessRuleViolationException;
import com.rajnish.razorpay.common.exceptions.DuplicateResourceException;
import com.rajnish.razorpay.common.exceptions.ResourceNotFoundException;
import com.rajnish.razorpay.payment.dto.request.CreateOrderRequest;
import com.rajnish.razorpay.payment.dto.response.OrderResponse;
import com.rajnish.razorpay.payment.dto.response.PaymentResponse;
import com.rajnish.razorpay.payment.entity.OrderRecord;
import com.rajnish.razorpay.payment.entity.Payments;
import com.rajnish.razorpay.payment.mapper.OrderMapper;
import com.rajnish.razorpay.payment.mapper.PaymentMapper;
import com.rajnish.razorpay.payment.repositories.OrderRepository;
import com.rajnish.razorpay.payment.repositories.PaymentRepository;
import com.rajnish.razorpay.payment.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final OrderMapper orderMapper;

    @Value("${order.default-expiry-minutes:30}")
    private int defaultOrderExpiryMinutes;

    @Override
    @Transactional
    public OrderResponse createOrder(UUID merchantId, CreateOrderRequest request) {
        if (request.receipt() != null && orderRepository.existsByMerchantIdAndReceipt(merchantId,request.receipt())){
            throw new DuplicateResourceException("DUPLICATE_ORDER_RECEIPT","Order with receipt already exists : "+request.receipt());
        }

        OrderRecord order=OrderRecord.builder()
                .receipt(request.receipt())
                .amount(request.amount())
                .notes(request.notes())

                .merchantId(merchantId)
                .orderStatus(OrderStatus.CREATED)
                .expiresAt(request.expiresAt() !=null ? request.expiresAt() :
                        LocalDateTime.now().plusMinutes(defaultOrderExpiryMinutes))
                .build();

        order=orderRepository.save(order);

        return orderMapper.toOrderResponse(order);
    }

    @Override
    public OrderResponse getById(UUID merchantId, UUID orderId) {
        OrderRecord order= orderRepository.findByIdAndMerchantId(merchantId,orderId)
                .orElseThrow(()->new ResourceNotFoundException("order", orderId));
        return orderMapper.toOrderResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(UUID merchantId, UUID orderId) {
        OrderRecord order= orderRepository.findByIdAndMerchantId(merchantId,orderId)
                .orElseThrow(()->new ResourceNotFoundException("order", orderId));

        if (order.getOrderStatus()==OrderStatus.CANCELED || order.getOrderStatus()==OrderStatus.PAID) {
            throw new BusinessRuleViolationException("ORDER_CANNOT_BE_CANCELED",
                    "Order cannot be canceled in current state : "+order.getOrderStatus().name());
        }
        order.setOrderStatus(OrderStatus.CANCELED);
        order=orderRepository.save(order);

        return orderMapper.toOrderResponse(order);
    }

    @Override
    public List<PaymentResponse> listPayments(UUID merchantId, UUID orderId) {
        OrderRecord order= orderRepository.findByIdAndMerchantId(merchantId,orderId)
                .orElseThrow(()->new ResourceNotFoundException("order", orderId));

        List<Payments> paymentsList= paymentRepository.findByOrder_Id(order);

//        return paymentsList.stream().map(
//                payments->paymentMapper.toResponse(payments)
//        ).collect(Collectors.toList());

        return paymentMapper.toResponseList(paymentsList);
    }
}

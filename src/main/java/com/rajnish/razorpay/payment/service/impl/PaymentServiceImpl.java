package com.rajnish.razorpay.payment.service.impl;

import com.rajnish.razorpay.common.enums.OrderStatus;
import com.rajnish.razorpay.common.enums.PaymentStatus;
import com.rajnish.razorpay.common.exceptions.BusinessRuleViolationException;
import com.rajnish.razorpay.common.exceptions.ResourceNotFoundException;
import com.rajnish.razorpay.payment.dto.request.PaymentInitRequest;
import com.rajnish.razorpay.payment.dto.response.PaymentResponse;
import com.rajnish.razorpay.payment.entity.OrderRecord;
import com.rajnish.razorpay.payment.entity.Payments;
import com.rajnish.razorpay.payment.gateway.PaymentGatewayRouter;
import com.rajnish.razorpay.payment.gateway.dto.PaymentRequest;
import com.rajnish.razorpay.payment.gateway.dto.PaymentResult;
import com.rajnish.razorpay.payment.mapper.PaymentMapper;
import com.rajnish.razorpay.payment.repositories.OrderRepository;
import com.rajnish.razorpay.payment.repositories.PaymentRepository;
import com.rajnish.razorpay.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentGatewayRouter paymentGatewayRouter;
    private final PaymentMapper paymentMapper;

    @Override
    @Transactional
    public PaymentResponse initiate(UUID merchantId, PaymentInitRequest request) {

        OrderRecord order=orderRepository.findByIdAndMerchantId(request.orderId(),merchantId)
                .orElseThrow(()->new ResourceNotFoundException("Order", request.orderId()));

        if (order.getOrderStatus()!= OrderStatus.CREATED && order.getOrderStatus()!= OrderStatus.ATTEMPTED){
            throw new BusinessRuleViolationException("ORDER_NOT_PAYABLE",
                    "Order cannot accept payment in status: "+order.getOrderStatus());

        }

        order.setOrderStatus(OrderStatus.ATTEMPTED);
        order.setAttempts(order.getAttempts()+1);

        Payments payments=Payments.builder()
                .order(order)
                .merchantId(merchantId)
                .amount(order.getAmount())
                .status(PaymentStatus.CREATED)
                .method(request.method())
                .methodDetails(request.methodDetails())
                .build();

        payments=paymentRepository.save(payments);

        PaymentRequest paymentRequest=new PaymentRequest(
                payments.getId(),
                request.orderId(),
                merchantId,
                order.getAmount(),
                request.method(),
                request.methodDetails()
        );

        PaymentResult result=paymentGatewayRouter.initiate(paymentRequest);

        switch (result){
            case PaymentResult.Pending pending ->payments.setProcessorReference(pending.registrationRef());
            case PaymentResult.Failure failure ->{
                payments.setStatus(PaymentStatus.FAILED);
                payments.setErrorCode(failure.errorCode());
                payments.setErrorMessage(failure.errorDescription());
            }

        }

        payments=paymentRepository.save(payments);
        order=orderRepository.save(order);




        return paymentMapper.toResponse(payments);
    }
}

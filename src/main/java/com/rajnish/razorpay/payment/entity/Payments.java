package com.rajnish.razorpay.payment.entity;

import com.rajnish.razorpay.common.entity.Money;
import com.rajnish.razorpay.common.enums.PaymentMethod;
import com.rajnish.razorpay.common.enums.PaymentStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "payments")
public class Payments {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "order_id",nullable = false)
    private OrderRecord record;

    @Column(nullable = false)
    private UUID merchantId;

    @Embedded
    private Money  amount;

    @Column(nullable = false,length = 100)
    private String idempotencyKey;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 50)
    private PaymentStatus  status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 50)
    private PaymentMethod method;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb",name = "method_details")
    private Map<String,Object> methodDetails;

    @Column(length = 100)
    private String bankReference;

    @Column(length = 100)
    private String errorCode;

    @Column(length = 200)
    private String errorMessage;

    private LocalDateTime authorizedAt;

    private LocalDateTime capturedAt;

    private LocalDateTime failedAt;

    private LocalDateTime refundedAt;

    private LocalDateTime settledAt;




}

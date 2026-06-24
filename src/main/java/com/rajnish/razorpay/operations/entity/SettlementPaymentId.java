package com.rajnish.razorpay.operations.entity;

import com.rajnish.razorpay.common.entity.BaseEntity;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;

import java.util.UUID;

@Embeddable
public class SettlementPaymentId {

    private UUID settlementId;

    private UUID paymentId;
}

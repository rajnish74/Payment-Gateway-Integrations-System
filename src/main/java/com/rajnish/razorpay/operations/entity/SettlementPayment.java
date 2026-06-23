package com.rajnish.razorpay.operations.entity;

import com.rajnish.razorpay.common.entity.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "settlement_payments")
public class SettlementPayment {

    @EmbeddedId
    private SettlementPaymentId id;

    @MapsId("settlementId")
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "settlement_id",nullable = false)
    private Settlement settlement;


}

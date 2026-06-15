package com.rajnish.razorpay.payment.entity;

import com.rajnish.razorpay.common.enums.PaymentStatus;
import com.rajnish.razorpay.merchant.entity.Merchant;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "merchant_webhook_config")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MerchantWebhookConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(nullable = false,name = "merchant_id")
    private Merchant merchant;

    @Column(nullable = false,length = 500)
    private String targetUrl;  //www.zara.com/webhook/success

    @Column(length = 500)
    private String webhookSecretHash;

    @Column(nullable = false)
    private Boolean enabled=true;

    @Column(length = 200)
    private String eventType;   //cooma seperated list of event types to subscribe to




}

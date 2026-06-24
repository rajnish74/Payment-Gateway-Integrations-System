package com.rajnish.razorpay.payment.config;

import com.rajnish.razorpay.common.enums.PaymentMethod;
import com.rajnish.razorpay.payment.gateway.PaymentAdapter;
import com.rajnish.razorpay.payment.gateway.adapter.CardPaymentAdapter;
import com.rajnish.razorpay.payment.gateway.adapter.UPIPaymentAdapter;
import org.springframework.context.annotation.Bean;

import java.util.Map;

public class PaymentAdapterConfig {

    @Bean
    public Map<PaymentMethod, PaymentAdapter> paymentAdapterMap(){
        return Map.of(
                PaymentMethod.CARD,new CardPaymentAdapter(),
                PaymentMethod.NETBANKING,new CardPaymentAdapter(),
                PaymentMethod.UPI,new UPIPaymentAdapter()
        );
    }
}

package com.rajnish.razorpay.payment.config;

import com.rajnish.razorpay.common.enums.PaymentMethod;
import com.rajnish.razorpay.payment.processor.PaymentProcessor;
import com.rajnish.razorpay.payment.processor.dto.PaymentProcessorResponse;
import com.rajnish.razorpay.payment.processor.strategy.CardPaymentProcessor;
import com.rajnish.razorpay.payment.processor.strategy.NetBankingPaymentProcessor;
import com.rajnish.razorpay.payment.processor.strategy.UPIPaymentProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class PaymentProcessorConfig {

    @Bean
    public Map<PaymentMethod, PaymentProcessor> paymentProcessorMap() {
        return Map.of(
                PaymentMethod.CARD,new CardPaymentProcessor(),
                PaymentMethod.NETBANKING,new NetBankingPaymentProcessor(),
                PaymentMethod.UPI,new UPIPaymentProcessor()
        );
    }

}

package com.rajnish.razorpay.payment.config;

import com.rajnish.razorpay.common.enums.PaymentMethod;
import com.rajnish.razorpay.payment.processor.PaymentProcessor;
import com.rajnish.razorpay.payment.processor.dto.PaymentProcessorResponse;
import com.rajnish.razorpay.payment.processor.strategy.CardPaymentProcessor;
import com.rajnish.razorpay.payment.processor.strategy.NetBankingPaymentProcessor;
import com.rajnish.razorpay.payment.processor.strategy.UPIPaymentProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class PaymentProcessorConfig {

    private final CardPaymentProcessor cardPaymentProcessor;
    private final UPIPaymentProcessor upiPaymentProcessor;
    private final NetBankingPaymentProcessor netBankingPaymentProcessor;

    @Bean
    public Map<PaymentMethod, PaymentProcessor> paymentProcessorMap() {
        return Map.of(
                PaymentMethod.CARD,cardPaymentProcessor,
                PaymentMethod.NETBANKING,netBankingPaymentProcessor,
                PaymentMethod.UPI,upiPaymentProcessor
        );
    }

}

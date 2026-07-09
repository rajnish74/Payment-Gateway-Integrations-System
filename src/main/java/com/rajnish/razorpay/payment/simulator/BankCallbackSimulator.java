package com.rajnish.razorpay.payment.simulator;

import com.rajnish.razorpay.common.enums.PaymentStatus;
import com.rajnish.razorpay.payment.entity.Payments;
import com.rajnish.razorpay.payment.repositories.PaymentRepository;
import com.rajnish.razorpay.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class BankCallbackSimulator {

    private final PaymentRepository  paymentRepository;
    private final PaymentService paymentService;
    private final SimulatorConfig simulatorConfig;

    @Scheduled(fixedDelayString = "${simulator.callback.poll-interval-ms:5000}")
    public void processCallbacks(){
        LocalDateTime globalWindow=LocalDateTime.now().minusSeconds(1);

        List<Payments> candidates=paymentRepository.findByStatusAndCreatedAtBefore(PaymentStatus.AUTHORIZING,globalWindow);

        if(candidates.isEmpty()){
            return;
        }

        for(Payments payments:candidates){
            simulateCallback(payments);
        }
    }

    private void simulateCallback(Payments payments) {

    }

}

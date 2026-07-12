package com.rajnish.razorpay.payment.statemachine;

import com.rajnish.razorpay.common.enums.PaymentActor;
import com.rajnish.razorpay.common.enums.PaymentEvent;
import com.rajnish.razorpay.common.enums.PaymentStatus;
import com.rajnish.razorpay.payment.entity.PaymentTransitionLog;
import com.rajnish.razorpay.payment.entity.Payments;
import com.rajnish.razorpay.payment.repositories.PaymentTransitionLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentTransitionLogService {
    private final PaymentTransitionLogRepository paymentTransitionLogRepository;
    private final PaymentStateMachine paymentStateMachine;

    public PaymentStatus apply(Payments  payments, PaymentEvent event) {
        PaymentStatus next=paymentStateMachine.transition(payments.getStatus(),event);


        PaymentTransitionLog log=PaymentTransitionLog.builder()
                .payments(payments)
                .fromStatus(payments.getStatus())
                .toStatus(next)
                .event(event)
                .actor(PaymentActor.SYSTEM)
                .occurredAt(LocalDateTime.now())
                .build();

        payments.setStatus(next);
        paymentTransitionLogRepository.save(log);

        return next;
    }
}

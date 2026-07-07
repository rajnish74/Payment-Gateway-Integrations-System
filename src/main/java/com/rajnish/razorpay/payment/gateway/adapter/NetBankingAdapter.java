package com.rajnish.razorpay.payment.gateway.adapter;

import com.rajnish.razorpay.common.enums.PaymentMethod;
import com.rajnish.razorpay.payment.gateway.PaymentAdapter;
import com.rajnish.razorpay.payment.gateway.dto.PaymentRequest;
import com.rajnish.razorpay.payment.gateway.dto.PaymentResult;
import com.rajnish.razorpay.payment.processor.PaymentProcessorRouter;
import com.rajnish.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.rajnish.razorpay.payment.processor.dto.PaymentProcessorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("NETBANKING")
@RequiredArgsConstructor
@Slf4j
public class NetBankingAdapter implements PaymentAdapter {

    private final PaymentProcessorRouter  paymentProcessorRouter;

    @Override
    public PaymentResult initiate(PaymentRequest request){
        log.info("Initiating Payment with NetBankingAdapter, paymentId: {}",request.paymentId());

        try {
        PaymentProcessorRequest paymentProcessorRequest=PaymentProcessorRequest.nonCard(
                request.paymentId(),
                PaymentMethod.NETBANKING,
                request.amount(),
                request.methodDetails()
        );


        PaymentProcessorResponse paymentProcessorResponse=
                paymentProcessorRouter.charge(paymentProcessorRequest);

        return switch (paymentProcessorResponse){
            case PaymentProcessorResponse.Failure failure->
                    new PaymentResult.Failure(failure.errorCode(),failure.errorDescription());

            case PaymentProcessorResponse.Pending pending->
                    new PaymentResult.Pending(pending.processorReference());

            case PaymentProcessorResponse.Success success->
                    new PaymentResult.Success(success.bankReference());
        };
        }catch (Exception e){
            log.warn("NetBanking failed, paymentId: {}",request.paymentId());
            return new PaymentResult.Failure("NBK_FAILED",e.getMessage());
        }
    }

    @Override
    public PaymentResult capture(UUID paymentId) {
        return new PaymentResult.Success("NBK_REFERENCE");
    }
}

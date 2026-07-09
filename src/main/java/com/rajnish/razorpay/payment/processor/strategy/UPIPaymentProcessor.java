package com.rajnish.razorpay.payment.processor.strategy;

import com.rajnish.razorpay.common.utils.RandomizerUtil;
import com.rajnish.razorpay.payment.processor.PaymentProcessor;
import com.rajnish.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.rajnish.razorpay.payment.processor.dto.PaymentProcessorResponse;

public class UPIPaymentProcessor implements PaymentProcessor {
    @Override
    public PaymentProcessorResponse charge(PaymentProcessorRequest request) {
        final String VPA_CODE_FAIL="fail@axis";
        String bankCode=request.methodDetails() != null ?
                request.methodDetails().get("VPA").toString() : null;

        //simulation
        if(VPA_CODE_FAIL.equals(bankCode)){
            return new PaymentProcessorResponse.Failure(
                    "UPI_FAILED",
                    "Bank failed to process the payment"
            );
        }

        String processorRef="UPI_PROCESSOR_"+ RandomizerUtil.randomBase64(16);

        return new PaymentProcessorResponse.Pending(processorRef);
    }
}

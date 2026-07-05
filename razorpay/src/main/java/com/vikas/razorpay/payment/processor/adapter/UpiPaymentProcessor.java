package com.vikas.razorpay.payment.processor.adapter;

import com.vikas.razorpay.common.util.RandomizerUtil;
import com.vikas.razorpay.payment.processor.PaymentProcessor;
import com.vikas.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.vikas.razorpay.payment.processor.dto.PaymentProcessorResponse;
import org.springframework.stereotype.Component;

@Component
public class UpiPaymentProcessor implements PaymentProcessor {


    @Override
    public PaymentProcessorResponse charge(PaymentProcessorRequest request) {

        final String VPA_CODE_FAIL ="fail@okaxis";

        // Call the third party
        String bankCode=request.methodDetails()!=null?
                request.methodDetails().get("BANK").toString(): VPA_CODE_FAIL;

        if(VPA_CODE_FAIL.equals(bankCode)){
            return new PaymentProcessorResponse.Failure("UPI_REJECTED","UPI rejected the transaction registration");
        }

        String processorRef="UPI_PROCESSOR"+ RandomizerUtil.randomBase64(16);
        String bankRef="BANK_REF"+ RandomizerUtil.randomBase64(16);


        return new PaymentProcessorResponse.Success(processorRef,bankRef);
    }
}

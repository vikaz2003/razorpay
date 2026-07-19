package com.vikas.razorpay.payment.processor;


import com.vikas.razorpay.common.enums.PaymentMethod;
import com.vikas.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.vikas.razorpay.payment.processor.dto.PaymentProcessorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
@RequiredArgsConstructor
public class PaymentProcessorRouter {

    private final Map<PaymentMethod,PaymentProcessor> paymentProcessorMap;

    public PaymentProcessorResponse charge(PaymentProcessorRequest req){
           PaymentProcessor processor=paymentProcessorMap.get(req.method());
           if(processor ==null){
               throw new IllegalArgumentException("No Payment Processor registered for method: "+req.method());
           }
           return processor.charge(req);
    }


}

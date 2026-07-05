package com.vikas.razorpay.payment.processor.adapter;

import com.vikas.razorpay.payment.processor.PaymentProcessor;
import com.vikas.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.vikas.razorpay.payment.processor.dto.PaymentProcessorResponse;

public class UpiPaymentProcessor implements PaymentProcessor {


    @Override
    public PaymentProcessorResponse charge(PaymentProcessorRequest request) {
        return null;
    }
}

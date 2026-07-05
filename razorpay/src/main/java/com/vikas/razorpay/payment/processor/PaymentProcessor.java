package com.vikas.razorpay.payment.processor;

import com.vikas.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.vikas.razorpay.payment.processor.dto.PaymentProcessorResponse;

public interface PaymentProcessor {

    PaymentProcessorResponse charge(PaymentProcessorRequest request);
}

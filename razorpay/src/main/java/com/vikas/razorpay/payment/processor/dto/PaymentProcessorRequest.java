package com.vikas.razorpay.payment.processor.dto;

import com.vikas.razorpay.common.entity.Money;
import com.vikas.razorpay.common.enums.PaymentMethod;

import java.util.Map;

public record PaymentProcessorRequest(
        PaymentMethod method,
        Money amount,
        Map<String,Object> methodDetails
) {
}

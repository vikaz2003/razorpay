package com.vikas.razorpay.payment.dto.request;

import com.vikas.razorpay.common.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;

import java.util.Map;
import java.util.UUID;

public record PaymentInItRequestDto(
        @NotNull(message = "OrderId is required")
        UUID orderId,

        @NotNull(message = "Payment Method is required")
        PaymentMethod method,
        Map<String,Object> methodDetails

) {
}

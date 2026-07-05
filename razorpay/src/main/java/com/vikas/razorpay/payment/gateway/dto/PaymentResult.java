package com.vikas.razorpay.payment.gateway.dto;

public sealed interface PaymentResult permits PaymentResult.Pending
        , PaymentResult.Failure{

    record Pending(String registrationRef) implements PaymentResult {}


    record Failure(String error,String errorDescription) implements PaymentResult {}
}

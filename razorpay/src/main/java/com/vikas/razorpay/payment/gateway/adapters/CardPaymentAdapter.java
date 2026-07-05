package com.vikas.razorpay.payment.gateway.adapters;

import com.vikas.razorpay.payment.gateway.PaymentAdapter;
import com.vikas.razorpay.payment.gateway.dto.PaymentRequest;
import com.vikas.razorpay.payment.gateway.dto.PaymentResult;

import java.util.UUID;

public class CardPaymentAdapter implements PaymentAdapter {


    @Override
    public PaymentResult initiate(PaymentRequest request) {
       return null;
    }

    @Override
    public PaymentResult capture(UUID paymentId) {
        return null;
    }
}

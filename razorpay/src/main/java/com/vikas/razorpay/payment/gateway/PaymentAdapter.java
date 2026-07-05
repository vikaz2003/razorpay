package com.vikas.razorpay.payment.gateway;

import com.vikas.razorpay.payment.dto.response.PaymentResponse;
import com.vikas.razorpay.payment.gateway.dto.PaymentRequest;

public interface PaymentAdapter {

    void initiate(PaymentRequest request);

}

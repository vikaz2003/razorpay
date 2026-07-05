package com.vikas.razorpay.payment.gateway;

import com.vikas.razorpay.payment.dto.response.PaymentResponse;
import com.vikas.razorpay.payment.gateway.dto.PaymentRequest;
import com.vikas.razorpay.payment.gateway.dto.PaymentResult;

public interface PaymentAdapter {

    PaymentResult initiate(PaymentRequest request);

}

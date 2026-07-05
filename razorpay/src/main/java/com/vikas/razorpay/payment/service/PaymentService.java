package com.vikas.razorpay.payment.service;

import com.vikas.razorpay.payment.dto.request.PaymentInItRequestDto;
import com.vikas.razorpay.payment.dto.response.PaymentResponse;

import java.util.UUID;

public interface PaymentService {

    PaymentResponse  initiate(UUID merchantId, PaymentInItRequestDto request);
}

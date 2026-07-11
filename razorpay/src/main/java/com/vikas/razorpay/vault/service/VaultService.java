package com.vikas.razorpay.vault.service;

import com.vikas.razorpay.common.entity.Money;
import com.vikas.razorpay.payment.processor.dto.PaymentProcessorResponse;
import com.vikas.razorpay.vault.dto.request.TokenizeRequest;
import com.vikas.razorpay.vault.dto.response.TokenizeResponse;
import jakarta.validation.Valid;

import java.util.Map;
import java.util.UUID;

public interface VaultService {

    TokenizeResponse tokenize(@Valid TokenizeRequest request, java.util.UUID merchantId);

    PaymentProcessorResponse charge(UUID paymentId, String token, Money amount, Map<String, Object> methodDetails);
}

package com.vikas.razorpay.payment.controller;


import com.vikas.razorpay.payment.dto.request.PaymentInItRequestDto;
import com.vikas.razorpay.payment.dto.response.PaymentResponse;
import com.vikas.razorpay.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/v1/payments")
@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    UUID merchantId=UUID.fromString("5b00ef6b-3668-446f-8ba4-3ab6f5cd5348"); // TODO: replace it with MerchantContext


    @PostMapping
    public ResponseEntity<PaymentResponse> initiate(@Valid @RequestBody PaymentInItRequestDto request){
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.initiate(merchantId,request));
    }

    @PostMapping("/{paymentId}/capture")
    public ResponseEntity<PaymentResponse> capture(@PathVariable UUID paymentId){
        return ResponseEntity.ok(paymentService.capture(merchantId,paymentId));
    }
}

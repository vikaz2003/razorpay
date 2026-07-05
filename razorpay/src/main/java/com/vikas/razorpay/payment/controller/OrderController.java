package com.vikas.razorpay.payment.controller;


import com.vikas.razorpay.payment.dto.request.CreateOrderRequest;
import com.vikas.razorpay.payment.dto.response.OrderResponse;
import com.vikas.razorpay.payment.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    UUID merchantId=UUID.fromString("d7de7437-a115-410f-983e-3b4bd2e16638"); // TODO: replace it with MerchantContext

    @PostMapping
    public ResponseEntity<OrderResponse> create(@RequestBody @Valid CreateOrderRequest createOrderRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.create(merchantId,createOrderRequest));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getByOrderId(@PathVariable UUID orderId){
        return ResponseEntity.ok(orderService.getById(merchantId, orderId));
    }


}

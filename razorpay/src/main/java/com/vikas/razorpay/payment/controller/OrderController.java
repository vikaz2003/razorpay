package com.vikas.razorpay.payment.controller;


import com.vikas.razorpay.payment.dto.request.CreateOrderRequest;
import com.vikas.razorpay.payment.dto.response.OrderResponse;
import com.vikas.razorpay.payment.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> create(@RequestBody @Valid CreateOrderRequest createOrderRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService(createOrderRequest));
    }
}

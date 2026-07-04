package com.vikas.razorpay.payment.service;

import com.vikas.razorpay.payment.dto.request.CreateOrderRequest;
import com.vikas.razorpay.payment.dto.response.OrderResponse;
import com.vikas.razorpay.payment.dto.response.PaymentResponse;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

public interface OrderService {
   OrderResponse create(UUID merchantId, CreateOrderRequest createOrderRequest);

   OrderResponse getById(UUID merchantId,UUID orderId);

   OrderResponse cancel(UUID merchantId,UUID orderId);


}

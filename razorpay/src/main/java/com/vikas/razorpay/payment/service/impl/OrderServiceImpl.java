package com.vikas.razorpay.payment.service.impl;

import com.vikas.razorpay.common.enums.OrderStatus;
import com.vikas.razorpay.common.exception.DuplicateResourceException;
import com.vikas.razorpay.common.exception.ResourceNotFoundException;
import com.vikas.razorpay.payment.dto.request.CreateOrderRequest;
import com.vikas.razorpay.payment.dto.response.OrderResponse;
import com.vikas.razorpay.payment.entity.OrderRecord;
import com.vikas.razorpay.payment.repository.OrderRepository;
import com.vikas.razorpay.payment.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {


    private final OrderRepository orderRepository;

    @Override
    public OrderResponse create(UUID merchantId, CreateOrderRequest createOrderRequest) {
        if(createOrderRequest.receipt()!=null && orderRepository.existsByMerchantIdAndReceipt(merchantId, createOrderRequest.receipt())){
            throw new DuplicateResourceException("ORDER_RECEIPT_DUPLICATE","order cannot be duplicate");
        }

        OrderRecord orderRecord=OrderRecord.builder()
                .receipt(createOrderRequest.receipt())
                .amount(createOrderRequest.amount())
                .notes(createOrderRequest.notes())
                .merchantId(merchantId)
                .orderStatus(OrderStatus.CREATED)
                .expiresAt(LocalDateTime.now().plusMinutes(30))
                .build();


        orderRecord=orderRepository.save(orderRecord);


        //send kafka event about order is created

        return new OrderResponse(orderRecord.getId(),orderRecord.getMerchantId(),orderRecord.getReceipt(),orderRecord.getAmount(),orderRecord.getOrderStatus(),orderRecord.getAttempts(),orderRecord.getNotes(),orderRecord.getExpiresAt(),null);
    }

    @Override
    public OrderResponse getById(UUID merchantId, UUID orderId) {
        OrderRecord orderRecord=
                orderRepository.findByIdAndMerchantId(orderId,merchantId).orElseThrow(new ResourceNotFoundException("Order"+orderId));
        return new OrderResponse(orderRecord.getId(),orderRecord.getMerchantId(),orderRecord.getReceipt(),orderRecord.getAmount(),orderRecord.getOrderStatus(),orderRecord.getAttempts(),orderRecord.getNotes(),orderRecord.getExpiresAt(),orderRecord.getExpiresAt());
    }

    @Override
    public OrderResponse cancel(UUID merchantId, UUID orderId) {
        return
    }
}

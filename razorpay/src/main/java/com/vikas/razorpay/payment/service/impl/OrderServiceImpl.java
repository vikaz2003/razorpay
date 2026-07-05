package com.vikas.razorpay.payment.service.impl;

import com.vikas.razorpay.common.enums.OrderStatus;
import com.vikas.razorpay.common.exception.BusinessRuleViolationException;
import com.vikas.razorpay.common.exception.DuplicateResourceException;
import com.vikas.razorpay.common.exception.ResourceNotFoundException;
import com.vikas.razorpay.payment.dto.request.CreateOrderRequest;
import com.vikas.razorpay.payment.dto.response.OrderResponse;
import com.vikas.razorpay.payment.dto.response.PaymentResponse;
import com.vikas.razorpay.payment.entity.OrderRecord;
import com.vikas.razorpay.payment.entity.Payment;
import com.vikas.razorpay.payment.mapper.OrderMapper;
import com.vikas.razorpay.payment.mapper.PaymentMapper;
import com.vikas.razorpay.payment.repository.OrderRepository;
import com.vikas.razorpay.payment.repository.PaymentRepository;
import com.vikas.razorpay.payment.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {


    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
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
                .expiresAt(createOrderRequest.expiresAt()!=null ?createOrderRequest.expiresAt() :LocalDateTime.now().plusMinutes(30))
                .build();


        orderRecord=orderRepository.save(orderRecord);


        //send kafka event about order is created

        return orderMapper.toResponse(orderRecord);
    }

    @Override
    public OrderResponse getById(UUID merchantId, UUID orderId) {
        OrderRecord orderRecord=
                orderRepository.findByIdAndMerchantId(orderId,merchantId).orElseThrow(()->new ResourceNotFoundException("Order Not Present with orderId: "+orderId,"Order"));
        return orderMapper.toResponse(orderRecord);
    }

    @Override
    @Transactional
    public OrderResponse cancel(UUID merchantId, UUID orderId) {
        OrderRecord orderRecord=
                orderRepository.findByIdAndMerchantId(orderId,merchantId).orElseThrow(()->new ResourceNotFoundException("Order Not Present with orderId: "+orderId,"Order"));
        if(orderRecord.getOrderStatus()==OrderStatus.CANCELLED|| orderRecord.getOrderStatus()==OrderStatus.PAID){
             throw new BusinessRuleViolationException("Cannot cancel order with status: "+ orderRecord.getOrderStatus(),"ORDER_CANNOT_CANCEL");
        }
        orderRecord.setOrderStatus(OrderStatus.CANCELLED);
        orderRecord=orderRepository.save(orderRecord);
        return orderMapper.toResponse(orderRecord);
    }

    @Override
    public List<PaymentResponse> listPayments(UUID merchantId, UUID orderId) {
        OrderRecord order=orderRepository.findByIdAndMerchantId(orderId,merchantId).orElseThrow(()->new ResourceNotFoundException("Order Not Present with orderId: "+orderId,"Order"));
        List<Payment> paymentList=paymentRepository.findByOrder_Id(orderId);
        return paymentMapper.toResponseList(paymentList);
    }
}

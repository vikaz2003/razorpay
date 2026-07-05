package com.vikas.razorpay.payment.service.impl;

import com.vikas.razorpay.common.enums.OrderStatus;
import com.vikas.razorpay.common.enums.PaymentStatus;
import com.vikas.razorpay.common.exception.BusinessRuleViolationException;
import com.vikas.razorpay.common.exception.ResourceNotFoundException;
import com.vikas.razorpay.payment.dto.request.PaymentInItRequestDto;
import com.vikas.razorpay.payment.dto.response.PaymentResponse;
import com.vikas.razorpay.payment.entity.OrderRecord;
import com.vikas.razorpay.payment.entity.Payment;
import com.vikas.razorpay.payment.gateway.PaymentGatewayRouter;
import com.vikas.razorpay.payment.gateway.dto.PaymentRequest;
import com.vikas.razorpay.payment.repository.OrderRepository;
import com.vikas.razorpay.payment.repository.PaymentRepository;
import com.vikas.razorpay.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentGatewayRouter router;

    @Override
    @Transactional
    public PaymentResponse initiate(UUID merchantId, PaymentInItRequestDto request) {
        OrderRecord orderRecord=orderRepository.findByIdAndMerchantId(request.orderId(),merchantId)
                .orElseThrow(()-> new ResourceNotFoundException("Order notn found with id: "+request.orderId(),"ORDER_NOT_FOUND"));

        if(orderRecord.getOrderStatus()!= OrderStatus.CREATED && orderRecord.getOrderStatus()!=OrderStatus.ATTEMPTED){
            throw new BusinessRuleViolationException("ORDER_NOT_PAYABLE","Order cannot accept payment in status: "+orderRecord.getOrderStatus());
        }

        orderRecord.setOrderStatus(OrderStatus.ATTEMPTED);
        orderRecord.setAttempts(orderRecord.getAttempts()+1);
        Payment payment=Payment.builder()
                .order(orderRecord)
                .merchantId(merchantId)
                .status(PaymentStatus.CREATED)
                .method(request.method())
                .methodDetails(request.methodDetails())
                .amount(orderRecord.getAmount())
                .build();
        payment= paymentRepository.save(payment);
        PaymentRequest paymentRequest=new PaymentRequest(payment.getId(),request.orderId(),payment.getMerchantId(),payment.getAmount(),request.method(),request.methodDetails());
        router.initiate(paymentRequest);
        return null;
    }
}

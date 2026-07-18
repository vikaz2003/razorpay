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
import com.vikas.razorpay.payment.gateway.dto.PaymentResult;
import com.vikas.razorpay.payment.mapper.PaymentMapper;
import com.vikas.razorpay.payment.repository.OrderRepository;
import com.vikas.razorpay.payment.repository.PaymentRepository;
import com.vikas.razorpay.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentGatewayRouter router;
    private final PaymentMapper paymentMapper;

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
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
        PaymentResult result=router.initiate(paymentRequest);


        if(result instanceof PaymentResult.Pending(String registrationRef)){
             payment.setProcessorReference(registrationRef);
        }else if(result instanceof PaymentResult.Failure(String error, String errorDescription)){
               payment.setStatus(PaymentStatus.FAILED);
               payment.setErrorCode(error);
               payment.setErrorDescription(errorDescription);
        }else if(result instanceof PaymentResult.Success success){
               payment.setStatus(PaymentStatus.SETTLED);


        }
        payment=paymentRepository.save(payment);
        orderRepository.save(orderRecord);
        return paymentMapper.toResponse(payment);
    }

    @Override
    public PaymentResponse capture(UUID merchantId, UUID paymentId) {
        Payment payment=paymentRepository.findByIdAndMerchantId(paymentId,merchantId)
                .orElseThrow(()-> new ResourceNotFoundException("Payment Not found for paymentId: "+paymentId,"PAYMENT"));
        payment.setStatus(PaymentStatus.CAPTURING);
        PaymentResult result=router.capture(payment.getMethod(),paymentId);
        if(result instanceof PaymentResult.Success success){
            log.info("Payment Captured,paymentId :{} ",paymentId);
            payment.setStatus(PaymentStatus.CAPTURED);
            payment.setCapturedAt(LocalDateTime.now());

        }else if(result instanceof PaymentResult.Failure(String error, String errorDescription)){
            log.warn("Payment capture failed,paymentId :{} ",paymentId);
            payment.setStatus(PaymentStatus.AUTHORIZED);
            payment.setErrorCode(error);
            payment.setErrorDescription(errorDescription);
        }
        payment=paymentRepository.save(payment);
        return paymentMapper.toResponse(payment);
    }

    @Override
    @Transactional
    public void resolveAuthorization(UUID id, boolean approve, String bankRef, String errorCode, String errorDescription) {
         Payment payment=paymentRepository.findById(id).orElseThrow(
                 ()-> new ResourceNotFoundException("Payment not found with id: "+id,"PAYMENT")
         );
         if(payment.getStatus()!=PaymentStatus.AUTHORIZING){
             return;
         }
         OrderRecord orderRecord=payment.getOrder();
         if(approve){
             payment.setBankReference(bankRef);
             payment.setAuthorizedAt(LocalDateTime.now());

             //Auto-capture
             PaymentResult paymentResult=router.capture(payment.getMethod(),id);
             if(paymentResult instanceof PaymentResult.Success success){
                 payment.setCapturedAt(LocalDateTime.now());
                 orderRecord.setOrderStatus(OrderStatus.PAID);
             }else if(paymentResult instanceof PaymentResult.Failure FAILURE){
                 payment.setErrorCode(errorCode);
                 payment.setErrorDescription(errorDescription);
             }else{
                 payment.setErrorDescription(errorDescription);
                 payment.setErrorCode(errorCode);
             }
             paymentRepository.save(payment);
             orderRepository.save(orderRecord);
         }
    }
}

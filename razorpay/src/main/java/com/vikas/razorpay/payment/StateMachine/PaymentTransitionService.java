package com.vikas.razorpay.payment.StateMachine;

import com.vikas.razorpay.common.enums.PaymentActor;
import com.vikas.razorpay.common.enums.PaymentEvent;
import com.vikas.razorpay.common.enums.PaymentStatus;
import com.vikas.razorpay.payment.entity.Payment;
import com.vikas.razorpay.payment.entity.PaymentTransitionLog;
import com.vikas.razorpay.payment.repository.PaymentTransitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentTransitionService {

    private final PaymentTransitionRepository paymentTransitionRepository;
    private final PaymentStateMachine paymentStateMachine;

    public PaymentStatus apply(Payment payment, PaymentEvent event) {
        PaymentStatus next = paymentStateMachine.transition(payment.getStatus(), event);
        payment.setStatus(next);
        PaymentTransitionLog log = PaymentTransitionLog.builder()
                .payment(payment)
                .fromStatus(payment.getStatus())
                .event(event)
                .toStatus(next)
                .actor(PaymentActor.SYSTEM) //TODO: fetch merchant context to identify actor
                .occurredAt(LocalDateTime.now())
                .build();

        paymentTransitionRepository.save(log);
        return next;
    }
}

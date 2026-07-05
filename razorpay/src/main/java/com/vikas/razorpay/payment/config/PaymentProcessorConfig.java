package com.vikas.razorpay.payment.config;


import com.vikas.razorpay.common.enums.PaymentMethod;
import com.vikas.razorpay.payment.gateway.PaymentAdapter;
import com.vikas.razorpay.payment.gateway.adapters.CardPaymentAdapter;
import com.vikas.razorpay.payment.gateway.adapters.NetBankingAdapter;
import com.vikas.razorpay.payment.gateway.adapters.UpiPaymentAdapter;
import com.vikas.razorpay.payment.processor.PaymentProcessor;
import com.vikas.razorpay.payment.processor.adapter.CardPaymentProcessor;
import com.vikas.razorpay.payment.processor.adapter.NetBankingPaymentProcessor;
import com.vikas.razorpay.payment.processor.adapter.UpiPaymentProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class PaymentProcessorConfig {

    @Bean
    public Map<PaymentMethod, PaymentProcessor> paymentProcessor(){
        return Map.of(
                PaymentMethod.CARD, new CardPaymentProcessor(),
                PaymentMethod.NETBANKING,new NetBankingPaymentProcessor(),
                PaymentMethod.UPI,new UpiPaymentProcessor()
        );
    }
}

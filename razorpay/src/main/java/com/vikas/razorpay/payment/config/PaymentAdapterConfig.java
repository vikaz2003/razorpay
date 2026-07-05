package com.vikas.razorpay.payment.config;


import com.vikas.razorpay.common.enums.PaymentMethod;
import com.vikas.razorpay.payment.gateway.PaymentAdapter;
import com.vikas.razorpay.payment.gateway.adapters.CardPaymentAdapter;
import com.vikas.razorpay.payment.gateway.adapters.NetBankingAdapter;
import com.vikas.razorpay.payment.gateway.adapters.UpiPaymentAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class PaymentAdapterConfig {

    @Bean
    public Map<PaymentMethod, PaymentAdapter> paymentAdapter(){
        return Map.of(
                PaymentMethod.CARD, new CardPaymentAdapter(),
                PaymentMethod.NETBANKING,new NetBankingAdapter(),
                PaymentMethod.UPI,new UpiPaymentAdapter()
        );
    }
}

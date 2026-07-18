package com.vikas.razorpay.merchant.security;


import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.UUID;

@Component
@Getter
@Setter
@RequestScope(proxyMode = ScopedProxyMode.INTERFACES)
public class MerchantContext {

    private UUID merchantId;
    private String keyId;
}

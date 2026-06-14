package com.vikas.razorpay.merchant.service;

import com.vikas.razorpay.merchant.dto.MerchantResponse;
import com.vikas.razorpay.merchant.dto.MerchantSignupRequest;
import org.jspecify.annotations.Nullable;

public interface AuthService {

    MerchantResponse signup(MerchantSignupRequest merchantSignupRequest);
}

package com.vikas.razorpay.merchant.service;

import com.vikas.razorpay.merchant.dto.response.MerchantResponse;
import com.vikas.razorpay.merchant.dto.request.MerchantSignupRequest;

public interface AuthService {

    MerchantResponse signup(MerchantSignupRequest merchantSignupRequest);
}

package com.vikas.razorpay.merchant.service;

import com.vikas.razorpay.merchant.dto.response.LoginResponseDto;
import com.vikas.razorpay.merchant.dto.request.LoginRequestDto;
import com.vikas.razorpay.merchant.dto.response.MerchantResponse;
import com.vikas.razorpay.merchant.dto.request.MerchantSignupRequest;
import jakarta.validation.Valid;

public interface AuthService {

    MerchantResponse signup(MerchantSignupRequest merchantSignupRequest);

    LoginResponseDto login(@Valid LoginRequestDto requestDto);
}

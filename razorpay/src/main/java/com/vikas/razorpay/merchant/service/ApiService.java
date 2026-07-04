package com.vikas.razorpay.merchant.service;

import com.vikas.razorpay.merchant.dto.request.CreateApiKeyRequest;
import com.vikas.razorpay.merchant.dto.response.ApiKeyCreateResponse;
import com.vikas.razorpay.merchant.dto.response.ApiKeyResponse;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

public interface ApiService {

    

     List<ApiKeyResponse> listByMerchant(UUID merchantId);

    void revoke(UUID merchantId, UUID keyId);

    ApiKeyCreateResponse rotate(UUID merchantId, UUID keyId);

    ApiKeyCreateResponse create(UUID merchantId, @Valid CreateApiKeyRequest request);
}

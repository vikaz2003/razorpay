package com.vikas.razorpay.merchant.service;

import com.vikas.razorpay.merchant.dto.ApiKeyCreateResponse;
import com.vikas.razorpay.merchant.dto.ApiKeyResponse;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public interface ApiService {

    ApiKeyCreateResponse create(UUID merchantId,CreateApiKeyRequest apiKeyRequest);

     List<ApiKeyResponse> listByMerchant(UUID merchantId);

    void revoke(UUID merchantId, UUID keyId);

    ApiKeyCreateResponse rotate(UUID merchantId, UUID keyId);
}

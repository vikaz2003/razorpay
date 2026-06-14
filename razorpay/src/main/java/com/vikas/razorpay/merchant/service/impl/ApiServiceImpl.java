package com.vikas.razorpay.merchant.service.impl;

import com.vikas.razorpay.common.exception.ResourceNotFoundException;
import com.vikas.razorpay.common.util.RandomizerUtil;
import com.vikas.razorpay.merchant.Entity.ApiKey;
import com.vikas.razorpay.merchant.Entity.Merchant;
import com.vikas.razorpay.merchant.dto.ApiKeyCreateResponse;
import com.vikas.razorpay.merchant.dto.ApiKeyResponse;
import com.vikas.razorpay.merchant.repo.ApiKeyRepository;
import com.vikas.razorpay.merchant.repo.AppUserRepository;
import com.vikas.razorpay.merchant.repo.MerchantRepository;
import com.vikas.razorpay.merchant.service.ApiService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
@Slf4j
@RequiredArgsConstructor
public class ApiServiceImpl implements ApiService {


    private final MerchantRepository merchantRepository;
    private final AppUserRepository appUserRepository;
    private final ApiKeyRepository apiKeyRepository;

    @Override
    public ApiKeyCreateResponse create(UUID merchantId, CreateApiKeyRequest apiKeyRequest) {
        Merchant merchant=merchantRepository.findById(merchantId)
                .orElseThrow(() -> new ResourceNotFoundException());
    }

    @Override
    public List<ApiKeyResponse> listByMerchant(UUID merchantId) {
        return apiKeyRepository.findByMerchant_Id(merchantId).stream()
                .map(apiKey-> new ApiKeyResponse(
                        apiKey.getId(),
                        apiKey.getKeyId(),
                        apiKey.getEnvironment(),
                        apiKey.isEnabled(),
                        apiKey.getLastUsedAt(),
                        null
                )).toList();
    }

    @Override
    public void revoke(UUID merchantId, UUID keyId) {
        ApiKey key=apiKeyRepository.findById(keyId)
                .filter(k -> k.getMerchant().getId().equals(merchantId))
                .orElseThrow(() -> new ResourceNotFoundException("ApiKey",keyId));
        key.setEnabled(false);
        apiKeyRepository.save(key);
    }

    @Override
    @Transactional
    public ApiKeyCreateResponse rotate(UUID merchantId, UUID keyId) {
        ApiKey key=apiKeyRepository.findById(keyId)
                .filter(k -> k.getMerchant().getId().equals(merchantId))
                .orElseThrow(() -> new ResourceNotFoundException("ApiKey",keyId));

        String newRawSecret= RandomizerUtil.randomBase64(40);
        key.setPreviousKeySecretHash(key.getKeySecretHash());
        key.setKeySecretHash(newRawSecret);
        key.setRotatedAt(LocalDateTime.now());
        key.setGracePeriodExpiresAt(LocalDateTime.now().plusHours(24));

        key=apiKeyRepository.save(key);
        return new ApiKeyCreateResponse(key.getId(),key.getKeyId(),newRawSecret,key.getEnvironment());
    }


}

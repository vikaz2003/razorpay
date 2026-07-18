package com.vikas.razorpay.merchant.controller;


import com.vikas.razorpay.merchant.dto.request.CreateApiKeyRequest;
import com.vikas.razorpay.merchant.dto.response.ApiKeyCreateResponse;
import com.vikas.razorpay.merchant.dto.response.ApiKeyResponse;
import com.vikas.razorpay.merchant.security.MerchantContext;
import com.vikas.razorpay.merchant.service.ApiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/merchants/api-keys")
@RequiredArgsConstructor
public class ApiKeyController {

    private final ApiService apiService;
    private final MerchantContext merchantContext;

    @PostMapping
    public  ResponseEntity<ApiKeyCreateResponse> create (@Valid @RequestBody CreateApiKeyRequest request){
           return ResponseEntity.status(HttpStatus.CREATED).body(apiService.create(merchantContext.getMerchantId(),request));
    }

    @GetMapping
    public ResponseEntity<List<ApiKeyResponse>> listByMerchant(){
        return ResponseEntity.ok(apiService.listByMerchant(merchantContext.getMerchantId()));
    }

    @DeleteMapping("/keyId")
    public ResponseEntity<Void> revoke(@PathVariable UUID keyId){
         apiService.revoke(merchantContext.getMerchantId(),keyId);
         return ResponseEntity.noContent().build();
    }

    @PostMapping("/{keyId}/rotate")
    public ResponseEntity<ApiKeyCreateResponse> rotateKey(@PathVariable String keyId){
        return ResponseEntity.ok(apiService.rotate(merchantContext.getMerchantId(),keyId));
    }

}

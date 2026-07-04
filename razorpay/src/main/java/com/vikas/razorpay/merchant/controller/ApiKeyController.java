package com.vikas.razorpay.merchant.controller;


import com.vikas.razorpay.merchant.dto.request.CreateApiKeyRequest;
import com.vikas.razorpay.merchant.dto.response.ApiKeyCreateResponse;
import com.vikas.razorpay.merchant.dto.response.ApiKeyResponse;
import com.vikas.razorpay.merchant.service.ApiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/merchants/{merchantId}/api-keys")
@RequiredArgsConstructor
public class ApiKeyController {

    private final ApiService apiService;

    @PostMapping
    public  ResponseEntity<ApiKeyCreateResponse> create (@PathVariable UUID merchantId, @Valid @RequestBody CreateApiKeyRequest request){
           return ResponseEntity.status(HttpStatus.CREATED).body(apiService.create(merchantId,request));
    }

    @GetMapping
    public ResponseEntity<List<ApiKeyResponse>> listByMerchant(UUID merchantId){
        return ResponseEntity.status(201).body(apiService.listByMerchant(merchantId));
    }

    @DeleteMapping("/keyId")
    public ResponseEntity<Void> revoke(@PathVariable UUID merchantId,@PathVariable UUID keyId){
         apiService.revoke(merchantId,keyId);
         return ResponseEntity.noContent().build();
    }

    @PostMapping("/{keyId}/rotate")
    public ResponseEntity<ApiKeyCreateResponse> rotateKey(@PathVariable UUID merchantId, @PathVariable UUID keyId){
        return ResponseEntity.status(201).body(apiService.rotate(merchantId,keyId));
    }

}

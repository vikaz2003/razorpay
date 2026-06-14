package com.vikas.razorpay.merchant.controller;


import com.vikas.razorpay.merchant.dto.ApiKeyCreateResponse;
import com.vikas.razorpay.merchant.dto.ApiKeyResponse;
import com.vikas.razorpay.merchant.service.ApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.GetExchange;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/merchants/{merchantId}/api-keys")
@RequiredArgsConstructor
public class ApiKeyController {

    private final ApiService apiService;

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

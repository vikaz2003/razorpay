package com.vikas.razorpay.merchant.dto;

import com.vikas.razorpay.common.enums.Environment;

import java.time.LocalDateTime;
import java.util.UUID;

public record ApiKeyResponse(
        UUID id,
        String keyId,
        String keySecretHash,
       Environment environment,
        Boolean Enabled,
        LocalDateTime lastUsedAt
) {

}

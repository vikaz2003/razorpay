package com.vikas.razorpay.merchant.dto;

import com.vikas.razorpay.common.enums.Environment;

import java.util.UUID;

public record ApiKeyCreateResponse(

        UUID id,
        String KeyId,
        String KeySecretHash,
        Environment environment

) {
}

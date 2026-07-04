package com.vikas.razorpay.merchant.dto.response;

import com.vikas.razorpay.common.enums.Environment;

import java.util.UUID;

public record ApiKeyCreateResponse(

        UUID id,
        String KeyId,
        String KeySecret,
        Environment environment

) {
}

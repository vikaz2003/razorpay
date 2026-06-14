package com.vikas.razorpay.merchant.dto;

import com.vikas.razorpay.common.enums.BusinessType;
import com.vikas.razorpay.common.enums.MerchantStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;


public record MerchantResponse(
        UUID id,
        String name,
        String email,
        String businessName,
        BusinessType businessType,
        MerchantStatus merchantStatus
) {


}

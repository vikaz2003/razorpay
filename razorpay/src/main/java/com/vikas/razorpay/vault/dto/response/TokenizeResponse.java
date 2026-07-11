package com.vikas.razorpay.vault.dto.response;

import com.vikas.razorpay.common.enums.CardBrand;

public record TokenizeResponse(
        String token,
        String lastFour,
        CardBrand brand,
        Integer expiryMonth,
        Integer expiryYear
) {


}

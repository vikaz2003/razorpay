package com.vikas.razorpay.merchant.mapper;

import com.vikas.razorpay.merchant.Entity.Merchant;
import com.vikas.razorpay.merchant.dto.request.MerchantSignupRequest;
import com.vikas.razorpay.merchant.dto.response.MerchantResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MerchantMapper {

    Merchant toEntityFromSignUpRequest(MerchantSignupRequest request);
    @Mapping(source = "status",target = "merchantStatus")
    MerchantResponse toResponse(Merchant merchant);

}

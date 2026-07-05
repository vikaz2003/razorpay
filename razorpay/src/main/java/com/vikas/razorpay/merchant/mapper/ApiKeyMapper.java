package com.vikas.razorpay.merchant.mapper;

import com.vikas.razorpay.merchant.Entity.ApiKey;
import com.vikas.razorpay.merchant.dto.response.ApiKeyCreateResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ApiKeyMapper {


    ApiKeyCreateResponse toCreateResponse(ApiKey apiKey);

}

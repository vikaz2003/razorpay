package com.vikas.razorpay.payment.mapper;

import com.vikas.razorpay.payment.dto.response.OrderResponse;
import com.vikas.razorpay.payment.entity.OrderRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {

    @Mapping(target = "status",source = "orderStatus")
    OrderResponse toResponse(OrderRecord order);
}

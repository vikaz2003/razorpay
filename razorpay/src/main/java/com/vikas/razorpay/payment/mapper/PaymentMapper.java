package com.vikas.razorpay.payment.mapper;

import com.vikas.razorpay.payment.dto.response.PaymentResponse;
import com.vikas.razorpay.payment.entity.Payment;
import jakarta.persistence.MappedSuperclass;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaymentMapper {

    @Mapping(target = "orderId",source="order.id")
    PaymentResponse toResponse(Payment payment);

    List<PaymentResponse> toResponseList(List<Payment> paymentList);
}

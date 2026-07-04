package com.vikas.razorpay.payment.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PaymentResponse(

) {
}

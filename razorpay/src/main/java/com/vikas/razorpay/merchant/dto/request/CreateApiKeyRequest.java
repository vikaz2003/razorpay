package com.vikas.razorpay.merchant.dto.request;


import com.vikas.razorpay.common.enums.Environment;
import lombok.Data;

@Data
public class CreateApiKeyRequest {

    Environment environment;
}

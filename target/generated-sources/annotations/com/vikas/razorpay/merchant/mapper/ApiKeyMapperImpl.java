package com.vikas.razorpay.merchant.mapper;

import com.vikas.razorpay.common.enums.Environment;
import com.vikas.razorpay.merchant.Entity.ApiKey;
import com.vikas.razorpay.merchant.dto.response.ApiKeyCreateResponse;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-11T22:16:20+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.1 (Oracle Corporation)"
)
@Component
public class ApiKeyMapperImpl implements ApiKeyMapper {

    @Override
    public ApiKeyCreateResponse toCreateResponse(ApiKey apiKey) {
        if ( apiKey == null ) {
            return null;
        }

        UUID id = null;
        Environment environment = null;

        id = apiKey.getId();
        environment = apiKey.getEnvironment();

        String keyId = null;
        String keySecret = null;

        ApiKeyCreateResponse apiKeyCreateResponse = new ApiKeyCreateResponse( id, keyId, keySecret, environment );

        return apiKeyCreateResponse;
    }
}

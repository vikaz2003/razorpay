package com.vikas.razorpay.vault.Mapper;

import com.vikas.razorpay.common.enums.CardBrand;
import com.vikas.razorpay.vault.dto.response.TokenizeResponse;
import com.vikas.razorpay.vault.entity.VaultCard;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-11T22:16:20+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.1 (Oracle Corporation)"
)
@Component
public class VaultMapperImpl implements VaultMapper {

    @Override
    public TokenizeResponse toTokenizeResponse(VaultCard vault) {
        if ( vault == null ) {
            return null;
        }

        String lastFour = null;
        CardBrand brand = null;
        Integer expiryMonth = null;
        Integer expiryYear = null;

        lastFour = vault.getLastFour();
        brand = vault.getBrand();
        if ( vault.getExpiryMonth() != null ) {
            expiryMonth = Integer.parseInt( vault.getExpiryMonth() );
        }
        if ( vault.getExpiryYear() != null ) {
            expiryYear = Integer.parseInt( vault.getExpiryYear() );
        }

        String token = null;

        TokenizeResponse tokenizeResponse = new TokenizeResponse( token, lastFour, brand, expiryMonth, expiryYear );

        return tokenizeResponse;
    }
}

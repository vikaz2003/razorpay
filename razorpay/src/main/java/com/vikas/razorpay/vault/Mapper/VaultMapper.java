package com.vikas.razorpay.vault.Mapper;


import com.vikas.razorpay.vault.dto.response.TokenizeResponse;
import com.vikas.razorpay.vault.entity.VaultCard;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VaultMapper {


    TokenizeResponse toTokenizeResponse(VaultCard vault);
}

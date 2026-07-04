package com.vikas.razorpay.merchant.service.impl;


import com.vikas.razorpay.common.enums.MerchantStatus;
import com.vikas.razorpay.common.enums.UserRole;
import com.vikas.razorpay.common.exception.DuplicateResourceException;
import com.vikas.razorpay.merchant.Entity.AppUser;
import com.vikas.razorpay.merchant.Entity.Merchant;
import com.vikas.razorpay.merchant.dto.response.MerchantResponse;
import com.vikas.razorpay.merchant.dto.request.MerchantSignupRequest;
import com.vikas.razorpay.merchant.repo.AppUserRepository;
import com.vikas.razorpay.merchant.repo.MerchantRepository;
import com.vikas.razorpay.merchant.service.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {


    private final AppUserRepository appUserRepository;
    private final MerchantRepository merchantRepository;

    @Override
    @Transactional
    public MerchantResponse signup(MerchantSignupRequest merchantSignupRequest) {
        if(merchantRepository.existsByEmail(merchantSignupRequest.email())){
            throw new DuplicateResourceException("Duplicate Merchant","Merchant Already exists with the email: "+merchantSignupRequest.email());
        }

        Merchant merchant=Merchant.builder()
                .businessName(merchantSignupRequest.businessName())
                .businessType(merchantSignupRequest.businessType())
                .email(merchantSignupRequest.email())
                .name(merchantSignupRequest.name())
                .status(MerchantStatus.PENDING_KYC)
                .build();

        merchantRepository.save(merchant);

        AppUser appUser=AppUser.builder()
                .email(merchantSignupRequest.email())
                .merchant(merchant)
                // TO DO ; encrypt usin gBcrypt
                .passwordHash(merchantSignupRequest.password())
                .role(UserRole.OWNER)
                .build();
        appUserRepository.save(appUser);
        return new MerchantResponse(merchant.getId(),merchant.getName(),merchant.getEmail(),merchant.getBusinessName(),merchant.getBusinessType(),merchant.getStatus());
    }
}

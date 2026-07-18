package com.vikas.razorpay.merchant.service.impl;


import com.vikas.razorpay.common.enums.MerchantStatus;
import com.vikas.razorpay.common.enums.UserRole;
import com.vikas.razorpay.common.exception.DuplicateResourceException;
import com.vikas.razorpay.common.exception.ResourceNotFoundException;
import com.vikas.razorpay.merchant.Entity.AppUser;
import com.vikas.razorpay.merchant.Entity.Merchant;
import com.vikas.razorpay.merchant.dto.request.LoginRequestDto;
import com.vikas.razorpay.merchant.dto.response.LoginResponseDto;
import com.vikas.razorpay.merchant.dto.response.MerchantResponse;
import com.vikas.razorpay.merchant.dto.request.MerchantSignupRequest;
import com.vikas.razorpay.merchant.mapper.MerchantMapper;
import com.vikas.razorpay.merchant.repo.AppUserRepository;
import com.vikas.razorpay.merchant.repo.MerchantRepository;
import com.vikas.razorpay.merchant.security.JwtUtil;
import com.vikas.razorpay.merchant.service.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {


    private final AppUserRepository appUserRepository;
    private final MerchantRepository merchantRepository;
    private final MerchantMapper merchantMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public MerchantResponse signup(MerchantSignupRequest merchantSignupRequest) {
        if(merchantRepository.existsByEmail(merchantSignupRequest.email())){
            throw new DuplicateResourceException("Duplicate Merchant","Merchant Already exists with the email: "+merchantSignupRequest.email());
        }

        Merchant merchant=merchantMapper.toEntityFromSignUpRequest(merchantSignupRequest);
        merchant.setStatus(MerchantStatus.PENDING_KYC);
        merchantRepository.save(merchant);

        AppUser appUser=AppUser.builder()
                .email(merchantSignupRequest.email())
                .merchant(merchant)
                .passwordHash(passwordEncoder.encode(merchantSignupRequest.password()))
                .role(UserRole.OWNER)
                .build();
        appUserRepository.save(appUser);
        return merchantMapper.toResponse(merchant);
    }

    @Override
    public LoginResponseDto login(LoginRequestDto requestDto) {
          authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestDto.email(),requestDto.password()));
          AppUser user=appUserRepository.findByEmail(requestDto.email())
                  .orElseThrow(()-> new ResourceNotFoundException("Email","Email"));
         String token=jwtUtil.generateAccessToken(requestDto.email(),user.getMerchant().getId(), requestDto.password());
         return new LoginResponseDto(token);
    }
}

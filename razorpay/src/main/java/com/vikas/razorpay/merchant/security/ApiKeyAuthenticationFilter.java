package com.vikas.razorpay.merchant.security;

import com.vikas.razorpay.common.exception.ResourceNotFoundException;
import com.vikas.razorpay.merchant.Entity.ApiKey;
import com.vikas.razorpay.merchant.repo.ApiKeyRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;


@Component
@Slf4j
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {


    private final ApiKeyRepository apiKeyRepository;
    private final BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
    private final MerchantContext merchantContext;
    private final HandlerExceptionResolver resolver;

    public ApiKeyAuthenticationFilter(
            ApiKeyRepository apiKeyRepository,
            @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver,
            MerchantContext merchantContext
    ) {
        this.apiKeyRepository=apiKeyRepository;
        this.resolver = resolver;
        this.merchantContext = merchantContext;

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
           log.info("Incoming Request: {}",request.getRequestURI());

           try {
               String header = request.getHeader("Authorization");
               if (header == null || !header.startsWith("Basic ")) {
                   filterChain.doFilter(request, response);
                   return;
               }
               // Authorization: Basic key rgdrghsifhiesf
               String[] credentials = decode(header);
               if (credentials == null) {
                   throw new BadRequestException("Malformed API KEY HEADER");
               }

               String keyId = credentials[0];
               String rawSecret = credentials[1];
               ApiKey apiKey = apiKeyRepository.findByKeyId(keyId)
                       .orElseThrow(() -> new BadRequestException("Invalid or missing API Key"));
               if (!apiKey.isEnabled() || !secretMatches(rawSecret, apiKey)) {
                   throw new BadRequestException("Invalid Api Key");
               }
               var auth = new UsernamePasswordAuthenticationToken(keyId, null,
                       List.of(new SimpleGrantedAuthority("API_KEY_ROLE")));
               SecurityContextHolder.getContext().setAuthentication(auth);
               merchantContext.setKeyId(apiKey.getKeyId());
               merchantContext.setMerchantId(apiKey.getMerchant().getId());
               filterChain.doFilter(request,response);
           } catch (Exception e) {
               resolver.resolveException(request,response,null,e);
           }
    }

    private String[] decode(String header){
        String encoded=header.substring("Basic ".length());
        String decoded=new String(Base64.getDecoder().decode(encoded), StandardCharsets.UTF_8);
        int colon=decoded.indexOf(":");
        if(colon<1) return null;
        return new String[]{decoded.substring(0,colon),decoded.substring(colon+1)};

    }

    private boolean secretMatches(String rawSecret,ApiKey apikey){
        if(passwordEncoder.matches(rawSecret, apikey.getKeySecretHash())){
            return true;
        }
        boolean isInGracePeriod=apikey.getGracePeriodExpiresAt()!=null &&
                LocalDateTime.now().isBefore(apikey.getGracePeriodExpiresAt());
        return isInGracePeriod && passwordEncoder.matches(rawSecret, apikey.getPreviousKeySecretHash());
    }
}

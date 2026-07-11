package com.vikas.razorpay.vault.service.impl;

import com.vikas.razorpay.common.entity.Money;
import com.vikas.razorpay.common.enums.CardBrand;
import com.vikas.razorpay.common.exception.ResourceNotFoundException;
import com.vikas.razorpay.common.util.RandomizerUtil;
import com.vikas.razorpay.payment.processor.PaymentProcessorRouter;
import com.vikas.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.vikas.razorpay.payment.processor.dto.PaymentProcessorResponse;
import com.vikas.razorpay.vault.Mapper.VaultMapper;
import com.vikas.razorpay.vault.config.VaultEncryption;
import com.vikas.razorpay.vault.dto.request.TokenizeRequest;
import com.vikas.razorpay.vault.dto.response.TokenizeResponse;
import com.vikas.razorpay.vault.entity.CardToken;
import com.vikas.razorpay.vault.entity.VaultCard;
import com.vikas.razorpay.vault.repo.CardTokenRepo;
import com.vikas.razorpay.vault.repo.VaultRepo;
import com.vikas.razorpay.vault.service.VaultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class VaultServiceImpl implements VaultService {

    private final CardTokenRepo cardTokenRepo;
    private final VaultRepo vaultRepo;
    private final VaultEncryption vaultEncryption;
    private  final BytesEncryptor bytesEncryptor;
    private final VaultMapper vaultMapper;
    private final PaymentProcessorRouter paymentProcessorRouter;

    @Override
    @Transactional
    public TokenizeResponse tokenize(TokenizeRequest request, UUID merchantId) {
          String lastFour= request.pan().substring(request.pan().length()-4);
          String bin=request.pan().substring(0,6);
          CardBrand cardBrand=detectBrand(request.pan());
          byte[] dek= KeyGenerators.secureRandom(32).generateKey();
          byte[] encryptedPan= VaultEncryption.panEncryptor(dek).encrypt(request.pan().getBytes(StandardCharsets.UTF_8));
          byte[] encryptedDek=bytesEncryptor.encrypt(dek);
        VaultCard vaultCard=VaultCard.builder()
                .brand(cardBrand)
                .bin(bin)
                .lastFour(lastFour)
                .encryptedDek(encryptedDek)
                .encryptedPan(encryptedPan)
                .expiryMonth(String.valueOf(request.expiryMonth()))
                .expiryYear(String.valueOf(request.expiryYear()))
                .build();
        String token="tok_"+ RandomizerUtil.randomBase64(32);
        vaultCard=vaultRepo.save(vaultCard);
        cardTokenRepo.save(CardToken.builder()
                .token(token)
                .vaultCard(vaultCard)
                .customer(request.customerId())
                .merchant(merchantId)
                .build());


        return new TokenizeResponse(token,lastFour,cardBrand, request.expiryMonth(), request.expiryYear());
    }

    @Override
    public PaymentProcessorResponse charge(UUID paymentId,String token, Money amount, Map<String, Object> methodDetails) {
         CardToken cardToken=cardTokenRepo.findByTokenAndRevokedAtIsNull(token)
                 .orElseThrow(() -> new ResourceNotFoundException("Card Token","Token"));
         VaultCard vaultCard=cardToken.getVaultCard();
         byte[] panBytes=null;
         try {
             byte[] dek = bytesEncryptor.decrypt(vaultCard.getEncryptedDek());
             panBytes = VaultEncryption.panEncryptor(dek).decrypt(vaultCard.getEncryptedPan());
             String pan = new String(panBytes, StandardCharsets.UTF_8);
             String expiry = vaultCard.getExpiryMonth() + "/" + vaultCard.getExpiryYear();
             PaymentProcessorRequest paymentProcessorRequest = PaymentProcessorRequest.card(
                     paymentId, pan, expiry, amount, methodDetails
             );

             PaymentProcessorResponse response = paymentProcessorRouter.charge(paymentProcessorRequest);
             log.info("Vault charge registered, token:{}", token.substring(0, 4));

             return response;
         }catch (Exception e){
             log.info("Vault charge failed, token:{}",token.substring(0,4));
             return new PaymentProcessorResponse.Failure("VAULT_CHARGE_FAILED",e.getLocalizedMessage());
         }finally{
             if(panBytes!=null){
                 Arrays.fill(panBytes, (byte) 0);
             }
         }

    }

    private CardBrand detectBrand(String pan){
        if (pan == null || pan.isEmpty()) {
            return CardBrand.UNKNOWN;
        }


        if (pan.matches("^4[0-9]{12}(?:[0-9]{3})?$")) {
            return CardBrand.VISA;
        }


        if (pan.matches("^(5[1-5][0-9]{14}|2(2[2-9][0-9]{12}|[3-6][0-9]{13}|7[01][0-9]{12}|720[0-9]{12}))$")) {
            return CardBrand.MASTERCARD;
        }


        if (pan.matches("^3[47][0-9]{13}$")) {
            return CardBrand.AMEX;
        }


        if (pan.matches("^(60[0-9]{14}|65[0-9]{14}|81[0-9]{14}|82[0-9]{14}|508[0-9]{13})$")) {
            return CardBrand.RUPAY;
        }

        return CardBrand.UNKNOWN;
    }
}

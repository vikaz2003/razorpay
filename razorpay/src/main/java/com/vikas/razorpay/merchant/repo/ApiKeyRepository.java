package com.vikas.razorpay.merchant.repo;

import com.vikas.razorpay.merchant.Entity.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.imageio.spi.ServiceRegistry;
import java.util.Collection;
import java.util.List;
import java.util.*;

public interface ApiKeyRepository extends JpaRepository<ApiKey, UUID> {


    List<ApiKey> findByMerchant_Id(UUID merchantId);

    ApiKey findByKeyId(String keyId);

    Optional<ApiKey> findByKeyIdAndMerchant_Id(String keyId,UUID merchantId);
}

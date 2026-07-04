package com.vikas.razorpay.merchant.repo;

import com.vikas.razorpay.merchant.Entity.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface ApiKeyRepository extends JpaRepository<ApiKey, UUID> {


    List<ApiKey> findByMerchant_Id(UUID merchantId);
}

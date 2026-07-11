package com.vikas.razorpay.vault.repo;

import com.vikas.razorpay.vault.entity.CardToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;


@Repository
public interface CardTokenRepo extends JpaRepository<CardToken, UUID> {
    Optional<CardToken> findByTokenAndRevokedAtIsNull(String token);
}

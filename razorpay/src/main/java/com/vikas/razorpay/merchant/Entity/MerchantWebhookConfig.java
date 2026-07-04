package com.vikas.razorpay.merchant.Entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "merchant_webhook_config")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MerchantWebhookConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;



    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(nullable = false,name ="merchant_id")
    private Merchant merchant;

    @Column(nullable = false,length = 500)
    private String targetUrl; // www.zera.com/webhook/success

    @Column(length = 255)
    private String webhookSecretHash;

    @Column(nullable = false)
    private Boolean enabled=true;

    @Column(length = 255)
    private String eventTypes;


}

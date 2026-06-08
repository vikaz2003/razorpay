package com.vikas.razorpay.payment.entity;


import com.vikas.razorpay.common.entity.Money;
import com.vikas.razorpay.common.enums.PaymentMethod;
import com.vikas.razorpay.common.enums.PaymentStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.cglib.core.Local;

import java.lang.classfile.FieldElement;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name="payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name="order_id",nullable = false)
    private OrderRecord order;
    @Column(nullable = false)
    private UUID merchantId;
    @Embedded
    private Money amount;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod method;
    @Column(nullable = false)
    private String idempotencyKey;
    @Column(length = 100)
    private String bankReference;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb",name="method_details")
    private Map<String,Object> methodDetails;
    @Column(length = 100)
    private String errorCode;
    @Column(length = 255)
    private String errorDescription;
    private LocalDateTime authorizedAt;
    private LocalDateTime capturedAt;
    private LocalDateTime failedAt;
    private LocalDateTime settledAt;
    private LocalDateTime refundedAt;
}

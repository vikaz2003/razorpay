package com.vikas.razorpay.operations.entity;


import com.vikas.razorpay.common.entity.BaseEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name="dlq_event")
public class DlqEvent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID merchantid;

    @OneToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(nullable = false,name="webhookId")
    private WebhookEvent webhookEvent;


    @Column(length = 1000)
    private String finalError;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false,columnDefinition = "jsonb")
    private Map<String,Object> payload;

    private LocalDateTime movedAt;


    private LocalDateTime replayedAt;
}

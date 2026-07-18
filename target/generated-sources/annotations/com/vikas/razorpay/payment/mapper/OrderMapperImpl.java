package com.vikas.razorpay.payment.mapper;

import com.vikas.razorpay.common.entity.Money;
import com.vikas.razorpay.common.enums.OrderStatus;
import com.vikas.razorpay.payment.dto.response.OrderResponse;
import com.vikas.razorpay.payment.entity.OrderRecord;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-19T01:38:23+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.1 (Oracle Corporation)"
)
@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public OrderResponse toResponse(OrderRecord order) {
        if ( order == null ) {
            return null;
        }

        UUID id = null;
        UUID merchantId = null;
        String receipt = null;
        Money amount = null;
        Integer attempts = null;
        Map<String, Object> notes = null;
        LocalDateTime expiresAt = null;
        LocalDateTime createdAt = null;

        id = order.getId();
        merchantId = order.getMerchantId();
        receipt = order.getReceipt();
        amount = order.getAmount();
        attempts = order.getAttempts();
        Map<String, Object> map = order.getNotes();
        if ( map != null ) {
            notes = new LinkedHashMap<String, Object>( map );
        }
        expiresAt = order.getExpiresAt();
        createdAt = order.getCreatedAt();

        OrderStatus status = null;

        OrderResponse orderResponse = new OrderResponse( id, merchantId, receipt, amount, status, attempts, notes, expiresAt, createdAt );

        return orderResponse;
    }
}

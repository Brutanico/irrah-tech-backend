package com.irrah.techbackend.dto;

import com.irrah.techbackend.domain.MessageStatus;
import java.math.BigDecimal;
import java.time.Instant;

public record SendMessageResponse(
        String id,
        MessageStatus status,
        Instant estimatedDelivery,
        BigDecimal cost,
        BigDecimal currentBalance
) {
}

package com.irrah.techbackend.dto;

import com.irrah.techbackend.domain.MessagePriority;
import com.irrah.techbackend.domain.MessageStatus;
import java.math.BigDecimal;
import java.time.Instant;

public record MessageResponse(
        String id,
        String conversationId,
        String senderId,
        String recipientId,
        String content,
        Instant timestamp,
        MessagePriority priority,
        MessageStatus status,
        BigDecimal cost
) {
}

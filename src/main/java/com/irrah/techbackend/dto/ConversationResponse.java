package com.irrah.techbackend.dto;

import java.time.Instant;

public record ConversationResponse(
        String id,
        String clientId,
        String recipientId,
        String recipientName,
        String lastMessageContent,
        Instant lastMessageTime,
        int unreadCount
) {
}

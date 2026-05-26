package com.irrah.techbackend.dto;

import com.irrah.techbackend.domain.MessageStatus;

public record MessageStatusResponse(
        String id,
        MessageStatus status
) {
}

package com.irrah.techbackend.dto;

import com.irrah.techbackend.domain.MessagePriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SendMessageRequest(
        @NotBlank String conversationId,
        @NotBlank String recipientId,
        @NotBlank
        @Size(max = 2000)
        String content,
        @NotNull MessagePriority priority
) {
}

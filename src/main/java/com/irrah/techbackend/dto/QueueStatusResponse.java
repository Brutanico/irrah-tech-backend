package com.irrah.techbackend.dto;

public record QueueStatusResponse(
        int normalQueueSize,
        int urgentQueueSize,
        int pendingMessages,
        long processedMessages,
        long failedMessages,
        boolean running
) {
}

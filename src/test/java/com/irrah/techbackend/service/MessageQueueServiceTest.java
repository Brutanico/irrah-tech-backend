package com.irrah.techbackend.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.irrah.techbackend.domain.MessagePriority;
import com.irrah.techbackend.entity.Message;
import org.junit.jupiter.api.Test;

class MessageQueueServiceTest {

    @Test
    void shouldPrioritizeUrgentMessages() {
        MessageQueueService queueService = new MessageQueueService(null);

        queueService.enqueue(message("normal-1", MessagePriority.NORMAL));
        queueService.enqueue(message("urgent-1", MessagePriority.URGENT));

        assertThat(queueService.pollNextMessageId()).contains("urgent-1");
        assertThat(queueService.pollNextMessageId()).contains("normal-1");
    }

    @Test
    void shouldAvoidNormalQueueStarvationAfterUrgentStreak() {
        MessageQueueService queueService = new MessageQueueService(null);

        queueService.enqueue(message("normal-1", MessagePriority.NORMAL));
        queueService.enqueue(message("urgent-1", MessagePriority.URGENT));
        queueService.enqueue(message("urgent-2", MessagePriority.URGENT));
        queueService.enqueue(message("urgent-3", MessagePriority.URGENT));
        queueService.enqueue(message("urgent-4", MessagePriority.URGENT));

        assertThat(queueService.pollNextMessageId()).contains("urgent-1");
        assertThat(queueService.pollNextMessageId()).contains("urgent-2");
        assertThat(queueService.pollNextMessageId()).contains("urgent-3");
        assertThat(queueService.pollNextMessageId()).contains("normal-1");
        assertThat(queueService.pollNextMessageId()).contains("urgent-4");
    }

    private Message message(String id, MessagePriority priority) {
        Message message = new Message();
        message.setId(id);
        message.setPriority(priority);
        return message;
    }
}

package com.irrah.techbackend.service;

import com.irrah.techbackend.domain.MessagePriority;
import com.irrah.techbackend.domain.MessageStatus;
import com.irrah.techbackend.dto.QueueStatusResponse;
import com.irrah.techbackend.entity.Message;
import com.irrah.techbackend.repository.MessageRepository;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MessageQueueService {

    private static final Logger log = LoggerFactory.getLogger(MessageQueueService.class);
    private static final int MAX_URGENT_STREAK = 3;

    private final Queue<String> normalQueue = new ConcurrentLinkedQueue<>();
    private final Queue<String> urgentQueue = new ConcurrentLinkedQueue<>();
    private final AtomicInteger urgentStreak = new AtomicInteger();
    private final AtomicLong processedMessages = new AtomicLong();
    private final AtomicLong failedMessages = new AtomicLong();
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final MessageRepository messageRepository;

    public MessageQueueService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void enqueue(Message message) {
        if (message.getPriority() == MessagePriority.URGENT) {
            urgentQueue.offer(message.getId());
        } else {
            normalQueue.offer(message.getId());
        }
    }

    @Scheduled(fixedDelayString = "${queue.worker.fixed-delay-ms:1000}")
    @Transactional
    public void processNext() {
        if (!running.get()) {
            return;
        }

        Optional<String> nextMessageId = pollNextMessageId();
        if (nextMessageId.isEmpty()) {
            return;
        }

        Message message = messageRepository.findById(nextMessageId.get()).orElse(null);
        if (message == null || message.getStatus() != MessageStatus.QUEUED) {
            return;
        }

        try {
            message.setStatus(MessageStatus.PROCESSING);
            messageRepository.flush();

            message.setStatus(MessageStatus.SENT);
            messageRepository.flush();

            message.setStatus(MessageStatus.DELIVERED);
            processedMessages.incrementAndGet();
        } catch (RuntimeException exception) {
            message.setStatus(MessageStatus.FAILED);
            failedMessages.incrementAndGet();
            log.error("Failed to process message {}", message.getId(), exception);
        }
    }

    public QueueStatusResponse status() {
        int normalSize = normalQueue.size();
        int urgentSize = urgentQueue.size();
        return new QueueStatusResponse(
                normalSize,
                urgentSize,
                normalSize + urgentSize,
                processedMessages.get(),
                failedMessages.get(),
                running.get()
        );
    }

    Optional<String> pollNextMessageId() {
        if (urgentQueue.isEmpty()) {
            urgentStreak.set(0);
            return Optional.ofNullable(normalQueue.poll());
        }

        if (!normalQueue.isEmpty() && urgentStreak.get() >= MAX_URGENT_STREAK) {
            urgentStreak.set(0);
            return Optional.ofNullable(normalQueue.poll());
        }

        String urgentMessageId = urgentQueue.poll();
        if (urgentMessageId != null) {
            urgentStreak.incrementAndGet();
        }
        return Optional.ofNullable(urgentMessageId);
    }
}

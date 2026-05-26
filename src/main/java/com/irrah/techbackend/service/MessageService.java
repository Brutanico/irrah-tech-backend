package com.irrah.techbackend.service;

import com.irrah.techbackend.domain.MessagePriority;
import com.irrah.techbackend.domain.MessageStatus;
import com.irrah.techbackend.domain.PlanType;
import com.irrah.techbackend.dto.MessageResponse;
import com.irrah.techbackend.dto.MessageStatusResponse;
import com.irrah.techbackend.dto.SendMessageRequest;
import com.irrah.techbackend.dto.SendMessageResponse;
import com.irrah.techbackend.entity.Client;
import com.irrah.techbackend.entity.Conversation;
import com.irrah.techbackend.entity.Message;
import com.irrah.techbackend.exception.BusinessException;
import com.irrah.techbackend.exception.ResourceNotFoundException;
import com.irrah.techbackend.repository.ClientRepository;
import com.irrah.techbackend.repository.ConversationRepository;
import com.irrah.techbackend.repository.MessageRepository;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final ClientRepository clientRepository;
    private final ConversationRepository conversationRepository;
    private final ConversationService conversationService;
    private final FinancialValidationService financialValidationService;
    private final MessageQueueService messageQueueService;
    private final MessageMapper messageMapper;

    public MessageService(
            MessageRepository messageRepository,
            ClientRepository clientRepository,
            ConversationRepository conversationRepository,
            ConversationService conversationService,
            FinancialValidationService financialValidationService,
            MessageQueueService messageQueueService,
            MessageMapper messageMapper
    ) {
        this.messageRepository = messageRepository;
        this.clientRepository = clientRepository;
        this.conversationRepository = conversationRepository;
        this.conversationService = conversationService;
        this.financialValidationService = financialValidationService;
        this.messageQueueService = messageQueueService;
        this.messageMapper = messageMapper;
    }

    @CacheEvict(cacheNames = {"clients", "balances"}, key = "#clientId")
    @Transactional
    public SendMessageResponse send(String clientId, SendMessageRequest request) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));
        Conversation conversation = conversationService.findOrCreateForSend(
                clientId,
                request.conversationId(),
                request.recipientId()
        );
        if (!conversation.getRecipientId().equals(request.recipientId())) {
            throw new BusinessException("Recipient does not match conversation");
        }

        BigDecimal cost = request.priority().cost();
        financialValidationService.validateAndCharge(client, cost);

        Instant now = Instant.now();
        Message message = new Message();
        message.setId("msg-" + UUID.randomUUID());
        message.setConversation(conversation);
        message.setSender(client);
        message.setRecipientId(request.recipientId());
        message.setContent(request.content());
        message.setTimestamp(now);
        message.setPriority(request.priority());
        message.setStatus(MessageStatus.QUEUED);
        message.setCost(cost);

        conversation.setLastMessageContent(request.content());
        conversation.setLastMessageTime(now);
        conversation.setUnreadCount(conversation.getUnreadCount() + 1);

        Message saved = messageRepository.save(message);
        conversationRepository.save(conversation);
        clientRepository.save(client);
        messageQueueService.enqueue(saved);

        BigDecimal currentBalance = client.getPlanType() == PlanType.PREPAID ? client.getBalance() : null;
        return new SendMessageResponse(
                saved.getId(),
                saved.getStatus(),
                now.plusSeconds(request.priority() == MessagePriority.URGENT ? 2 : 5),
                saved.getCost(),
                currentBalance
        );
    }

    @Transactional(readOnly = true)
    public List<MessageResponse> list(String clientId, MessageStatus status, MessagePriority priority) {
        List<Message> messages;
        if (status != null && priority != null) {
            messages = messageRepository.findBySender_IdAndStatusAndPriorityOrderByTimestampDesc(clientId, status, priority);
        } else if (status != null) {
            messages = messageRepository.findBySender_IdAndStatusOrderByTimestampDesc(clientId, status);
        } else if (priority != null) {
            messages = messageRepository.findBySender_IdAndPriorityOrderByTimestampDesc(clientId, priority);
        } else {
            messages = messageRepository.findBySender_IdOrderByTimestampDesc(clientId);
        }
        return messages.stream().map(messageMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public MessageResponse get(String clientId, String messageId) {
        return messageMapper.toResponse(findOwnedMessage(clientId, messageId));
    }

    @Transactional(readOnly = true)
    public MessageStatusResponse getStatus(String clientId, String messageId) {
        Message message = findOwnedMessage(clientId, messageId);
        return new MessageStatusResponse(message.getId(), message.getStatus());
    }

    @Transactional(readOnly = true)
    public List<MessageResponse> listConversationMessages(String clientId, String conversationId) {
        conversationService.findForClient(clientId, conversationId);
        return messageRepository.findByConversation_IdOrderByTimestampAsc(conversationId).stream()
                .map(messageMapper::toResponse)
                .toList();
    }

    private Message findOwnedMessage(String clientId, String messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found"));
        if (!message.getSender().getId().equals(clientId)) {
            throw new ResourceNotFoundException("Message not found");
        }
        return message;
    }
}

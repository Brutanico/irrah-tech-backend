package com.irrah.techbackend.service;

import com.irrah.techbackend.dto.ConversationResponse;
import com.irrah.techbackend.entity.Client;
import com.irrah.techbackend.entity.Conversation;
import com.irrah.techbackend.exception.ResourceNotFoundException;
import com.irrah.techbackend.repository.ConversationRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final ConversationMapper conversationMapper;
    private final ClientService clientService;

    public ConversationService(
            ConversationRepository conversationRepository,
            ConversationMapper conversationMapper,
            ClientService clientService
    ) {
        this.conversationRepository = conversationRepository;
        this.conversationMapper = conversationMapper;
        this.clientService = clientService;
    }

    @Transactional(readOnly = true)
    public List<ConversationResponse> listForClient(String clientId) {
        return conversationRepository.findByClient_IdOrderByLastMessageTimeDesc(clientId).stream()
                .map(conversationMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ConversationResponse getForClient(String clientId, String conversationId) {
        return conversationMapper.toResponse(findForClient(clientId, conversationId));
    }

    @Transactional(readOnly = true)
    public Conversation findForClient(String clientId, String conversationId) {
        return conversationRepository.findByIdAndClient_Id(conversationId, clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found"));
    }

    @Transactional
    public Conversation findOrCreateForSend(String clientId, String conversationId, String recipientId) {
        return conversationRepository.findByIdAndClient_Id(conversationId, clientId)
                .orElseGet(() -> {
                    Client client = clientService.findById(clientId);
                    Conversation conversation = new Conversation();
                    conversation.setId(conversationId);
                    conversation.setClient(client);
                    conversation.setRecipientId(recipientId);
                    conversation.setRecipientName(recipientId);
                    return conversationRepository.save(conversation);
                });
    }
}

package com.irrah.techbackend.service;

import com.irrah.techbackend.dto.ConversationResponse;
import com.irrah.techbackend.entity.Conversation;
import org.springframework.stereotype.Component;

@Component
public class ConversationMapper {

    public ConversationResponse toResponse(Conversation conversation) {
        return new ConversationResponse(
                conversation.getId(),
                conversation.getClient().getId(),
                conversation.getRecipientId(),
                conversation.getRecipientName(),
                conversation.getLastMessageContent(),
                conversation.getLastMessageTime(),
                conversation.getUnreadCount()
        );
    }
}

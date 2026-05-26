package com.irrah.techbackend.service;

import com.irrah.techbackend.dto.MessageResponse;
import com.irrah.techbackend.entity.Message;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {

    public MessageResponse toResponse(Message message) {
        return new MessageResponse(
                message.getId(),
                message.getConversation().getId(),
                message.getSender().getId(),
                message.getRecipientId(),
                message.getContent(),
                message.getTimestamp(),
                message.getPriority(),
                message.getStatus(),
                message.getCost()
        );
    }
}

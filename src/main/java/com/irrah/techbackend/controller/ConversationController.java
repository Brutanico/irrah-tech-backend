package com.irrah.techbackend.controller;

import com.irrah.techbackend.dto.ConversationResponse;
import com.irrah.techbackend.dto.MessageResponse;
import com.irrah.techbackend.service.ConversationService;
import com.irrah.techbackend.service.MessageService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/conversations")
public class ConversationController {

    private final ConversationService conversationService;
    private final MessageService messageService;

    public ConversationController(ConversationService conversationService, MessageService messageService) {
        this.conversationService = conversationService;
        this.messageService = messageService;
    }

    @GetMapping
    public ResponseEntity<List<ConversationResponse>> list(@RequestHeader("X-Client-Id") String clientId) {
        return ResponseEntity.ok(conversationService.listForClient(clientId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConversationResponse> get(
            @RequestHeader("X-Client-Id") String clientId,
            @PathVariable String id
    ) {
        return ResponseEntity.ok(conversationService.getForClient(clientId, id));
    }

    @GetMapping("/{id}/messages")
    public ResponseEntity<List<MessageResponse>> getMessages(
            @RequestHeader("X-Client-Id") String clientId,
            @PathVariable String id
    ) {
        return ResponseEntity.ok(messageService.listConversationMessages(clientId, id));
    }
}

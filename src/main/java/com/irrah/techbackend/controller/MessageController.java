package com.irrah.techbackend.controller;

import com.irrah.techbackend.domain.MessagePriority;
import com.irrah.techbackend.domain.MessageStatus;
import com.irrah.techbackend.dto.MessageResponse;
import com.irrah.techbackend.dto.MessageStatusResponse;
import com.irrah.techbackend.dto.SendMessageRequest;
import com.irrah.techbackend.dto.SendMessageResponse;
import com.irrah.techbackend.service.MessageService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public ResponseEntity<SendMessageResponse> send(
            @RequestHeader("X-Client-Id") String clientId,
            @Valid @RequestBody SendMessageRequest request
    ) {
        SendMessageResponse response = messageService.send(clientId, request);
        return ResponseEntity.created(URI.create("/messages/" + response.id())).body(response);
    }

    @GetMapping
    public ResponseEntity<List<MessageResponse>> list(
            @RequestHeader("X-Client-Id") String clientId,
            @RequestParam(required = false) MessageStatus status,
            @RequestParam(required = false) MessagePriority priority
    ) {
        return ResponseEntity.ok(messageService.list(clientId, status, priority));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MessageResponse> get(
            @RequestHeader("X-Client-Id") String clientId,
            @PathVariable String id
    ) {
        return ResponseEntity.ok(messageService.get(clientId, id));
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<MessageStatusResponse> getStatus(
            @RequestHeader("X-Client-Id") String clientId,
            @PathVariable String id
    ) {
        return ResponseEntity.ok(messageService.getStatus(clientId, id));
    }
}

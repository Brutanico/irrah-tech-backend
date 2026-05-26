package com.irrah.techbackend.controller;

import com.irrah.techbackend.dto.QueueStatusResponse;
import com.irrah.techbackend.service.MessageQueueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/queue")
public class QueueController {

    private final MessageQueueService messageQueueService;

    public QueueController(MessageQueueService messageQueueService) {
        this.messageQueueService = messageQueueService;
    }

    @GetMapping("/status")
    public ResponseEntity<QueueStatusResponse> status() {
        return ResponseEntity.ok(messageQueueService.status());
    }
}

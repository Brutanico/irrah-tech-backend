package com.irrah.techbackend.controller;

import com.irrah.techbackend.dto.BalanceResponse;
import com.irrah.techbackend.dto.ClientRequest;
import com.irrah.techbackend.dto.ClientResponse;
import com.irrah.techbackend.service.ClientService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public ResponseEntity<List<ClientResponse>> list() {
        return ResponseEntity.ok(clientService.list());
    }

    @PostMapping
    public ResponseEntity<ClientResponse> create(@Valid @RequestBody ClientRequest request) {
        ClientResponse response = clientService.create(request);
        return ResponseEntity.created(URI.create("/clients/" + response.id())).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> get(@PathVariable String id) {
        return ResponseEntity.ok(clientService.get(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponse> update(@PathVariable String id, @Valid @RequestBody ClientRequest request) {
        return ResponseEntity.ok(clientService.update(id, request));
    }

    @GetMapping("/{id}/balance")
    public ResponseEntity<BalanceResponse> getBalance(@PathVariable String id) {
        return ResponseEntity.ok(clientService.getBalance(id));
    }
}

package com.irrah.techbackend.service;

import com.irrah.techbackend.dto.AuthRequest;
import com.irrah.techbackend.dto.AuthResponse;
import com.irrah.techbackend.entity.Client;
import com.irrah.techbackend.exception.BusinessException;
import com.irrah.techbackend.exception.ResourceNotFoundException;
import com.irrah.techbackend.repository.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final ClientRepository clientRepository;

    public AuthService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Transactional(readOnly = true)
    public AuthResponse authenticate(AuthRequest request) {
        Client client = clientRepository.findByDocumentId(request.documentId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));
        if (!client.isActive()) {
            throw new BusinessException("Client is inactive");
        }
        return new AuthResponse(client.getId(), client.getName(), client.isActive());
    }
}

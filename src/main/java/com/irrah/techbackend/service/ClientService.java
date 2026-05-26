package com.irrah.techbackend.service;

import com.irrah.techbackend.domain.PlanType;
import com.irrah.techbackend.dto.BalanceResponse;
import com.irrah.techbackend.dto.ClientRequest;
import com.irrah.techbackend.dto.ClientResponse;
import com.irrah.techbackend.entity.Client;
import com.irrah.techbackend.exception.BusinessException;
import com.irrah.techbackend.exception.ConflictException;
import com.irrah.techbackend.exception.ResourceNotFoundException;
import com.irrah.techbackend.repository.ClientRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    public ClientService(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    @Transactional(readOnly = true)
    public List<ClientResponse> list() {
        return clientRepository.findAll().stream()
                .map(clientMapper::toResponse)
                .toList();
    }

    @Cacheable(cacheNames = "clients", key = "#id")
    @Transactional(readOnly = true)
    public ClientResponse get(String id) {
        return clientMapper.toResponse(findById(id));
    }

    @Cacheable(cacheNames = "balances", key = "#id")
    @Transactional(readOnly = true)
    public BalanceResponse getBalance(String id) {
        return clientMapper.toBalanceResponse(findById(id));
    }

    @CacheEvict(cacheNames = {"clients", "balances"}, allEntries = true)
    @Transactional
    public ClientResponse create(ClientRequest request) {
        validatePlan(request);
        if (clientRepository.existsByDocumentId(request.documentId())) {
            throw new ConflictException("A client with this document already exists");
        }

        Client client = new Client();
        client.setId("client-" + UUID.randomUUID());
        applyRequest(client, request);
        return clientMapper.toResponse(clientRepository.save(client));
    }

    @CacheEvict(cacheNames = {"clients", "balances"}, key = "#id")
    @Transactional
    public ClientResponse update(String id, ClientRequest request) {
        validatePlan(request);
        Client client = findById(id);
        clientRepository.findByDocumentId(request.documentId())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new ConflictException("A client with this document already exists");
                });

        applyRequest(client, request);
        return clientMapper.toResponse(client);
    }

    @Transactional(readOnly = true)
    public Client findById(String id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));
    }

    private void applyRequest(Client client, ClientRequest request) {
        client.setName(request.name());
        client.setDocumentId(request.documentId());
        client.setDocumentType(request.documentType());
        client.setPlanType(request.planType());
        client.setBalance(defaultMoney(request.balance()));
        client.setLimit(defaultMoney(request.limit()));
        client.setActive(request.active() == null || request.active());
    }

    private void validatePlan(ClientRequest request) {
        if (request.planType() == PlanType.PREPAID && defaultMoney(request.balance()).compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("Prepaid clients must have a non-negative balance");
        }
        if (request.planType() == PlanType.POSTPAID && defaultMoney(request.limit()).compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Postpaid clients must have a positive limit");
        }
    }

    private BigDecimal defaultMoney(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}

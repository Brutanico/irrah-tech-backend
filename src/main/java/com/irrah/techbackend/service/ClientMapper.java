package com.irrah.techbackend.service;

import com.irrah.techbackend.dto.BalanceResponse;
import com.irrah.techbackend.dto.ClientResponse;
import com.irrah.techbackend.entity.Client;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {

    public ClientResponse toResponse(Client client) {
        return new ClientResponse(
                client.getId(),
                client.getName(),
                client.getDocumentId(),
                client.getDocumentType(),
                client.getPlanType(),
                client.getBalance(),
                client.getLimit(),
                client.isActive()
        );
    }

    public BalanceResponse toBalanceResponse(Client client) {
        return new BalanceResponse(
                client.getId(),
                client.getPlanType(),
                client.getBalance(),
                client.getLimit()
        );
    }
}

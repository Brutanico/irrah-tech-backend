package com.irrah.techbackend.service;

import com.irrah.techbackend.domain.PlanType;
import com.irrah.techbackend.entity.Client;
import com.irrah.techbackend.exception.BusinessException;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;

@Service
public class FinancialValidationService {

    public void validateAndCharge(Client client, BigDecimal cost) {
        if (!client.isActive()) {
            throw new BusinessException("Client is inactive");
        }

        if (client.getPlanType() == PlanType.PREPAID) {
            if (client.getBalance().compareTo(cost) < 0) {
                throw new BusinessException("Insufficient balance");
            }
            client.setBalance(client.getBalance().subtract(cost));
            return;
        }

        if (client.getLimit().compareTo(cost) < 0) {
            throw new BusinessException("Insufficient postpaid limit");
        }
        client.setLimit(client.getLimit().subtract(cost));
    }
}

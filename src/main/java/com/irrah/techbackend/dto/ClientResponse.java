package com.irrah.techbackend.dto;

import com.irrah.techbackend.domain.DocumentType;
import com.irrah.techbackend.domain.PlanType;
import java.math.BigDecimal;

public record ClientResponse(
        String id,
        String name,
        String documentId,
        DocumentType documentType,
        PlanType planType,
        BigDecimal balance,
        BigDecimal limit,
        boolean active
) {
}

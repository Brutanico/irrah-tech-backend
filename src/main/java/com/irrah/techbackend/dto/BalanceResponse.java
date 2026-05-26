package com.irrah.techbackend.dto;

import com.irrah.techbackend.domain.PlanType;
import java.math.BigDecimal;

public record BalanceResponse(
        String clientId,
        PlanType planType,
        BigDecimal balance,
        BigDecimal limit
) {
}

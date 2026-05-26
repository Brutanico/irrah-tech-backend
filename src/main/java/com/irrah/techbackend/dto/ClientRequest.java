package com.irrah.techbackend.dto;

import com.irrah.techbackend.domain.DocumentType;
import com.irrah.techbackend.domain.PlanType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;

public record ClientRequest(
        @NotBlank String name,
        @NotBlank
        @Pattern(regexp = "\\d{11}|\\d{14}", message = "documentId must contain 11 or 14 digits")
        String documentId,
        @NotNull DocumentType documentType,
        @NotNull PlanType planType,
        @DecimalMin("0.00") BigDecimal balance,
        @DecimalMin("0.00") BigDecimal limit,
        Boolean active
) {
}

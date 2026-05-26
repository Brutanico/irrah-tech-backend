package com.irrah.techbackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AuthRequest(
        @NotBlank
        @Pattern(regexp = "\\d{11}|\\d{14}", message = "documentId must contain 11 or 14 digits")
        String documentId
) {
}

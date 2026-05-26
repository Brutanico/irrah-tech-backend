package com.irrah.techbackend.dto;

public record AuthResponse(
        String clientId,
        String name,
        boolean active
) {
}

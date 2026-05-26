package com.irrah.techbackend.domain;

import com.fasterxml.jackson.annotation.JsonValue;

public enum MessageStatus {
    QUEUED,
    PROCESSING,
    SENT,
    DELIVERED,
    READ,
    FAILED;

    @JsonValue
    public String toValue() {
        return name().toLowerCase();
    }
}

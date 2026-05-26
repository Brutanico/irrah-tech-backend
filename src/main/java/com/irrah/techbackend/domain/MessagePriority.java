package com.irrah.techbackend.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.math.BigDecimal;

public enum MessagePriority {
    NORMAL(new BigDecimal("0.25")),
    URGENT(new BigDecimal("0.50"));

    private final BigDecimal cost;

    MessagePriority(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal cost() {
        return cost;
    }

    @JsonCreator
    public static MessagePriority fromValue(String value) {
        return MessagePriority.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return name().toLowerCase();
    }
}

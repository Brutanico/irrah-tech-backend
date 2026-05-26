package com.irrah.techbackend.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PlanType {
    PREPAID,
    POSTPAID;

    @JsonCreator
    public static PlanType fromValue(String value) {
        return PlanType.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return name().toLowerCase();
    }
}

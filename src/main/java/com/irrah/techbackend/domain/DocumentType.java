package com.irrah.techbackend.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum DocumentType {
    CPF,
    CNPJ;

    @JsonCreator
    public static DocumentType fromValue(String value) {
        return DocumentType.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return name();
    }
}

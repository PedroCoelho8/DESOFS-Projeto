package com.portal.de.pagamentos.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Username {
    @Column(name="name", nullable = false)
    private String value;

    public Username(){}

    public Username(String value) {
        setValue(value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        if (value == null || !value.matches("[A-Za-zÀ-ÖØ-öø-ÿ\\s\\-]+")) {
            throw new IllegalArgumentException("Username must contain only letters, spaces, or hyphens.");
        }
        this.value = value;
    }

    public String toString() {
        return this.value;
    }
}

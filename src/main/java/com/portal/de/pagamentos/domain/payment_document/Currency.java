package com.portal.de.pagamentos.domain.payment_document;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Currency {

    @Column(name = "currency", nullable = false, length = 3)
    private String value;

    public Currency() {}

    public Currency(String value) {
        setValue(value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        if (value == null || !value.matches("[A-Z]{3}")) {
            throw new IllegalArgumentException("A moeda deve ser um código ISO 3 letras, ex: EUR, USD");
        }
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}

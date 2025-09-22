package com.portal.de.pagamentos.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class PhoneNumber {
    @Column(name="phoneNumber", unique=true, nullable = false)
    private String value;

    public PhoneNumber(){}

    public PhoneNumber(String value) {
        setValue(value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        if(value == null || !value.matches("\\d{9}")){
            throw new IllegalArgumentException("O número de telemóvel deve ter 9 dígitos");
        }

        if (!value.matches("^(21|22|91|92|93|96).*")) {
            throw new IllegalArgumentException("O número de telemóvel deve começar por 21, 22, 91, 92, 93 ou 96");
        }
        this.value = value;
    }

    public String toString() {
        return this.value;
    }
}

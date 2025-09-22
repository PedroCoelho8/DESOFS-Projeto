package com.portal.de.pagamentos.domain.bank_account;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.regex.Pattern;

@Embeddable
public class IBAN {

    @Column(name = "iban", unique = true, nullable = false)
    private String value;

    private static final Pattern IBAN_PATTERN = Pattern.compile("^[A-Z]{2}\\d{2}[A-Z0-9]{1,30}$");

    public IBAN() {}

    public IBAN(String value) {
        setValue(value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("IBAN inválido. Deve seguir o formato internacional (ex: PT50123456789012345678901)");
        }
        String trimmedValue = value.trim();
        if (trimmedValue.length() < 15 || trimmedValue.length() > 34) {
            throw new IllegalArgumentException("IBAN inválido. Deve ter entre 15 e 34 caracteres.");
        }
        if (!IBAN_PATTERN.matcher(trimmedValue).matches()) {
            throw new IllegalArgumentException("IBAN inválido. Deve seguir o formato internacional (ex: PT50123456789012345678901)");
        }
        this.value = trimmedValue;
    }

    public String toString() {
        return this.value;
    }

}
package com.portal.de.pagamentos.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.regex.Pattern;

@Embeddable
public class NIF {
    @Column(name="nif", unique = true, nullable = false)
    private String value;
    private static final int NIF_LENGTH = 9;
    private static final Pattern NUMERIC_PATTERN = Pattern.compile("^\\d+$");

    public NIF() {}

    public NIF(String value) {
        setNif(value);
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        if (value == null) {
            throw new IllegalArgumentException("NIF não pode ser nulo");
        }

        String trimmedValue = value.trim();

        if (trimmedValue.length() != NIF_LENGTH) {
            throw new IllegalArgumentException("NIF tem de ter 9 dígitos exatamente");
        }

        if (!NUMERIC_PATTERN.matcher(trimmedValue).matches()) {
            throw new IllegalArgumentException("NIF tem de ter apenas dígitos");
        }

        char firstDigit = trimmedValue.charAt(0);
        if (firstDigit != '1' && firstDigit != '2' && firstDigit != '5' &&
                firstDigit != '6' && firstDigit != '8' && firstDigit != '9') {
            throw new IllegalArgumentException("NIF tem de começar com 1, 2, 5, 6, 8, or 9");
        }

        if (!isValidChecksum(trimmedValue)) {
            throw new IllegalArgumentException("Invalid NIF checksum");
        }

        this.value = trimmedValue;
    }

    public void setNif(String value) {
        setValue(value);
    }
    private boolean isValidChecksum(String nif) {
        int sum = 0;

        for (int i = 0; i < NIF_LENGTH - 1; i++) {
            int digit = Character.getNumericValue(nif.charAt(i));
            sum += digit * (NIF_LENGTH - i);
        }

        int remainder = sum % 11;
        int expectedCheckDigit;

        if (remainder == 0 || remainder == 1) {
            expectedCheckDigit = 0;
        } else {
            expectedCheckDigit = 11 - remainder;
        }

        int actualCheckDigit = Character.getNumericValue(nif.charAt(NIF_LENGTH - 1));

        return expectedCheckDigit == actualCheckDigit;
    }

    @Override
    public String toString() {
        return this.value;
    }

}
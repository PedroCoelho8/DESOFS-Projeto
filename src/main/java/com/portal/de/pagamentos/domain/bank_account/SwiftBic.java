package com.portal.de.pagamentos.domain.bank_account;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.regex.Pattern;

@Embeddable
public class SwiftBic {

    @Column(name = "swiftBic", nullable = false)
    private String value;

    private static final Pattern SWIFT_PATTERN = Pattern.compile("^[A-Z]{6}[A-Z0-9]{2}([A-Z0-9]{3})?$");

    public SwiftBic() {}

    public SwiftBic(String value) {
        setValue(value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        if (value == null || !SWIFT_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("SWIFT/BIC inválido. Deve seguir o padrão internacional (ex: BCOMPTPLXXX)");
        }
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

}

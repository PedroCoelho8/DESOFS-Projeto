package com.portal.de.pagamentos.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.regex.Pattern;

@Embeddable
public class Email {

    @Column(name = "email", unique = true, nullable = false)
    private String value;

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^(?![.])[A-Za-z0-9+_.-]+(?<![.])@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    public Email() {}

    public Email(String value) {
        setValue(value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        if (value == null || !EMAIL_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Email é invalido. Deve conter [Alfanumericos]@[Alfanumericos].[com|pt]");
        }
        this.value = value;
    }

    public String toString() {
        return this.value;
    }
}

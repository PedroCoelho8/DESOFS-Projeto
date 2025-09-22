package com.portal.de.pagamentos.domain.bank_account;

import java.util.Arrays;
import java.util.Optional;

public enum AccountType {
    CHECKING("CHECKING", "Conta a ordem"),
    SAVINGS("SAVINGS", "Conta poupança"),
    BUSINESS("BUSINESS", "Conta empresarial"),
    JOINT("JOINT", "Conta conjunta");

    private final String code;
    private final String description;

    AccountType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static Optional<AccountType> findByCode(String code) {
        if (code == null) return Optional.empty();

        return Arrays.stream(values())
                .filter(type -> type.code.equalsIgnoreCase(code))
                .findFirst();
    }

    public static AccountType getByCode(String code) {
        return findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Nenhum tipo de conta encontrado para o código: " + code));
    }
}

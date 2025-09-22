package com.portal.de.pagamentos.domain.payment;

import java.util.Arrays;
import java.util.Optional;

public enum PaymentState {
    PENDING("PENDING", "Pendente"),
    PAYED("PAYED", "Pago"),
    CONFIRMED_TO_BE_PAID("CONFIRMED_TO_BE_PAID", "Confirmado para pagamento"),
    CANCELED("CANCELED", "Cancelado");

    private final String code;
    private final String description;

    PaymentState(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static Optional<PaymentState> findByCode(String code) {
        if (code == null) return Optional.empty();

        return Arrays.stream(values())
                .filter(state -> state.code.equalsIgnoreCase(code))
                .findFirst();
    }

    public static PaymentState getByCode(String code) {
        return findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Nenhum estado de pagamento encontrado para o código: " + code));
    }

    public boolean isFinalState() {
        return this == PAYED || this == CANCELED;
    }

    public boolean isCancelable() {
        return this == PENDING || this == CONFIRMED_TO_BE_PAID;
    }
}

package com.portal.de.pagamentos.domain.user;

import java.util.Arrays;
import java.util.Optional;

public enum Role {
    ADMIN("ADMIN", "Administrador de sistema"),
    GUEST("GUEST", "Convidado do sistema"),
    RECEIVERENTITY("RECEIVERENTITY", "Entidade recetora"),
    SENDERENTITY("SENDERENTITY", "Entidade enviadora");

    private final String code;
    private final String description;

    Role(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }
    public String getDescription() {
        return description;
    }

    public static Optional<Role> findByCode(String code) {
        if (code == null) {
            return Optional.empty();
        }

        return Arrays.stream(values())
                .filter(role -> role.code.equals(code))
                .findFirst();
    }
    public static Role getByCode(String code) {
        return findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("No role found for code: " + code));
    }
    public boolean hasAdminPrivileges() {
        return switch(this) {
            case ADMIN -> true;
            default -> false;
        };
    }

    public boolean hasSenderPrivileges() {
        return switch(this) {
            case SENDERENTITY , ADMIN -> true;
            default -> false;
        };
    }

    public boolean hasReceiverPrivileges() {
        return switch(this) {
            case SENDERENTITY , ADMIN -> true;
            default -> false;
        };
    }
}
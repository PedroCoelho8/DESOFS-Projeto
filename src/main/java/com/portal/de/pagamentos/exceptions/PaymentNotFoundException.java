package com.portal.de.pagamentos.exceptions;

public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

package com.portal.de.pagamentos.exceptions;

public class NotPendingPaymentException extends RuntimeException {
    public NotPendingPaymentException(String message) {
        super(message);
    }
}

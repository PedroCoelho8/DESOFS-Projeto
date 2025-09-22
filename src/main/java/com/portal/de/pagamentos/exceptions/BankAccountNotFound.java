package com.portal.de.pagamentos.exceptions;

public class BankAccountNotFound extends RuntimeException{
    public BankAccountNotFound(String message) {
        super(message);
    }
}

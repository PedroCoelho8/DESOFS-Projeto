package com.portal.de.pagamentos.exceptions;

public class UserNotDestined extends RuntimeException{
    public UserNotDestined(String message, Throwable cause) {
        super(message, cause);
    }
}

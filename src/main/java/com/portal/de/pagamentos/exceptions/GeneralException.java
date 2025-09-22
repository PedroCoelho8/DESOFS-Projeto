package com.portal.de.pagamentos.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GeneralException {

    public static final String USER_NOT_FOUND = "Utilizador não encontrado";


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException e) {
        Map<String, String> error = new HashMap<>();

        error.put("status", String.valueOf(HttpStatus.BAD_REQUEST.value()));
        error.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        error.put("message", e.getMessage());
        error.put("timestamp", String.valueOf(new Date()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception e) {
        Map<String, String> error = new HashMap<>();
        
        error.put("status", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
        error.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        error.put("message", e.getMessage());
        error.put("timestamp", String.valueOf(new Date()));

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
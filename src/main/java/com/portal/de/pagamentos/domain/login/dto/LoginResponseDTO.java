package com.portal.de.pagamentos.domain.login.dto;

import com.portal.de.pagamentos.domain.user.dto.UserDTO;

public class LoginResponseDTO {
    private String token;
    private UserDTO user;
    private boolean requiresTwoFA;
    private String message;

    // Default constructor
    public LoginResponseDTO() {}

    // Constructor for successful complete login
    public LoginResponseDTO(String token, UserDTO user) {
        this.token = token;
        this.user = user;
        this.requiresTwoFA = false;
        this.message = "Login successful";
    }

    // Constructor for 2FA required
    public LoginResponseDTO(boolean requiresTwoFA, String message) {
        this.requiresTwoFA = requiresTwoFA;
        this.message = message;
    }

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserDTO getUserDTO() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public boolean isRequiresTwoFA() {
        return requiresTwoFA;
    }

    public void setRequiresTwoFA(boolean requiresTwoFA) {
        this.requiresTwoFA = requiresTwoFA;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

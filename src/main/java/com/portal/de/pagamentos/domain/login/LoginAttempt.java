package com.portal.de.pagamentos.domain.login;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "login_attempts")
public class LoginAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="email", unique = true, nullable = false)
    private String email;

    @Column(name = "attempts")
    private int attempts = 0;

    @Column(name = "last_attempt")
    private LocalDateTime lastAttempt;

    public LoginAttempt() {}

    public LoginAttempt(String email) {
        this.email = email;
        this.attempts = 0;
    }

    // Getters and setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    // Fixed: Use proper increment method instead of setAttempts()
    public void incrementAttempts() {
        this.attempts++;
    }

    // Added: Method to reset attempts
    public void resetAttempts() {
        this.attempts = 0;
    }

    public LocalDateTime getLastAttempt() {
        return lastAttempt;
    }

    public void setLastAttempt(LocalDateTime lastAttempt) {
        this.lastAttempt = lastAttempt;
    }
}
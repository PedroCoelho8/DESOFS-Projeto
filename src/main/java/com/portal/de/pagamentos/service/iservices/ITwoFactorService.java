package com.portal.de.pagamentos.service.iservices;

public interface ITwoFactorService {
    void generateAndSendCode(String email);
    boolean verifyCode(String email, String code);
    void cleanupExpiredCodes();
}

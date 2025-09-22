package com.portal.de.pagamentos.service.iservices;

public interface IEmailService {
    void sendTwoFactorCode(String email, String code);
}
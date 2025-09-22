package com.portal.de.pagamentos.service;

import com.portal.de.pagamentos.config.AppConfig;
import com.portal.de.pagamentos.exceptions.EmailSendException;
import com.portal.de.pagamentos.service.iservices.IEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

@Service
public class EmailService implements IEmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final AppConfig appConfig;

    public EmailService(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @Override
    public void sendTwoFactorCode(String email, String code) {
        String deliveryMode = appConfig.getEmail().getDeliveryMode();

        String maskedEmail = maskEmail(email);
        logger.info("Sending 2FA code to {} using delivery mode: {}", maskedEmail, deliveryMode);

        switch (deliveryMode.toLowerCase()) {
            case "smtp":
                sendSmtpEmail(email, code);
                break;
            case "mailhog":
                sendMailhogEmail(email, code);
                break;
            default:
                logger.warn("Unknown delivery mode: {}. Using mailhog as fallback.", deliveryMode);
                sendMailhogEmail(email, code);
        }
    }

    private void sendSmtpEmail(String email, String code) {
        String maskedEmail = maskEmail(email);
        try {
            // Configure SMTP settings
            Properties props = new Properties();
            props.put("mail.smtp.host", appConfig.getEmail().getSmtp().getHost());
            props.put("mail.smtp.port", appConfig.getEmail().getSmtp().getPort());
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.ssl.trust", appConfig.getEmail().getSmtp().getHost());

            // Create session with authentication
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(
                            appConfig.getEmail().getSmtp().getUsername(),
                            appConfig.getEmail().getSmtp().getPassword()
                    );
                }
            });

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(appConfig.getEmail().getSmtp().getUsername()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Your Two-Factor Authentication Code");
            message.setContent(buildEmailContent(code), "text/html");

            Transport.send(message);
            logger.info("2FA code sent via SMTP to: {}", maskedEmail);

        } catch (MessagingException e) {
            logger.error("Failed to send SMTP email to: {}", maskedEmail, e);
            throw new EmailSendException("Failed to send verification code via SMTP", e);
        }
    }

    private void sendMailhogEmail(String email, String code) {
        String maskedEmail = maskEmail(email);
        try {
            // Configure mailhog-specific settings
            Properties props = new Properties();
            props.put("mail.smtp.host", appConfig.getEmail().getMailhog().getHost());
            props.put("mail.smtp.port", appConfig.getEmail().getMailhog().getPort());
            props.put("mail.smtp.auth", "false");
            props.put("mail.smtp.starttls.enable", "false");

            Session session = Session.getInstance(props);

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("noreply@portal-pagamentos.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Your Two-Factor Authentication Code");
            message.setContent(buildEmailContent(code), "text/html");

            Transport.send(message);
            logger.info("2FA code sent to Mailhog for: {}", maskedEmail);

        } catch (MessagingException e) {
            logger.error("Failed to send email to Mailhog for: {}", maskedEmail, e);
            throw new EmailSendException("Failed to send verification code to Mailhog", e);
        }
    }

    private String buildEmailContent(String code) {
        return String.format("""
            <html>
            <body>
                <h2>Portal de Pagamentos - Two-Factor Authentication</h2>
                <p>Your verification code is:</p>
                <h1 style="color: #007bff; font-size: 32px; letter-spacing: 5px;">%s</h1>
                <p>This code will expire in 5 minutes.</p>
                <p>If you didn't request this code, please ignore this email.</p>
            </body>
            </html>
            """, code);
    }

    private String maskEmail(String email) {
        int atIndex = email.indexOf('@');
        if (atIndex <= 1) return "[REDACTED]";
        return email.charAt(0)+ email.charAt(1) + email.charAt(2) + "****" + email.substring(atIndex);
    }
}
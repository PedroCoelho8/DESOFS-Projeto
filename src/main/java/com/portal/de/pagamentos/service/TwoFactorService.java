package com.portal.de.pagamentos.service;

import com.portal.de.pagamentos.config.AppConfig;
import com.portal.de.pagamentos.domain.login.TwoFactorCode;
import com.portal.de.pagamentos.exceptions.EmailSendException;
import com.portal.de.pagamentos.repositories.irepositories.ITwoFactorRepository;
import com.portal.de.pagamentos.service.iservices.ITwoFactorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class TwoFactorService implements ITwoFactorService {

    private static final Logger logger = LoggerFactory.getLogger(TwoFactorService.class);

    private final ITwoFactorRepository twoFactorCodeRepository;
    private final EmailService emailService;
    private final AppConfig appConfig;
    private final SecureRandom secureRandom;

    public TwoFactorService(ITwoFactorRepository twoFactorCodeRepository,
                            EmailService emailService,
                            AppConfig appConfig) {
        this.twoFactorCodeRepository = twoFactorCodeRepository;
        this.emailService = emailService;
        this.appConfig = appConfig;
        this.secureRandom = new SecureRandom();
    }

    @Override
    public void generateAndSendCode(String email) {
        try {
            twoFactorCodeRepository.deleteByEmail(email);

            // Generate 6-digit code
            String code = String.format("%06d", secureRandom.nextInt(1000000));

            // Save code to database
            TwoFactorCode twoFactorCode = new TwoFactorCode(
                    email,
                    code,
                    appConfig.getTwofa().getCodeExpiryMinutes()
            );
            twoFactorCodeRepository.save(twoFactorCode);

            // Send email
            emailService.sendTwoFactorCode(email, code);

            logger.info("O código multi-fator para o email " + email);
        } catch (Exception e) {
            logger.error("Erro ao gerar e enviar o código multi-fator para o email " + email);
            throw new EmailSendException("Erro ao gerar e enviar o código multi-fator para o email " + email, e);
        }
    }

    @Override
    public boolean verifyCode(String email, String inputCode) {
        try {
            Optional<TwoFactorCode> optionalCode = twoFactorCodeRepository.findByEmailAndUsedFalse(email);

            if (optionalCode.isEmpty()) {
                logger.error("O email " + email + " não tem nenhum código multifator associado");
                return false;
            }

            TwoFactorCode storedCode = optionalCode.get();

            // Check if code is expired
            if (storedCode.isExpired()) {
                logger.error("O código multifator expirou para o email " + email);
                twoFactorCodeRepository.delete(storedCode);
                return false;
            }

            // Check attempts
            if (storedCode.getAttempts() >= appConfig.getTwofa().getMaxAttempts()) {
                logger.error("Máximo de tentativas atingidas ao usar códigos para o email " + email);
                twoFactorCodeRepository.delete(storedCode);
                return false;
            }

            // Increment attempts
            storedCode.incrementAttempts();

            // Check if code matches
            if (!storedCode.getCode().equals(inputCode)) {
                twoFactorCodeRepository.save(storedCode);
                logger.error("Código invalido para o email "+ email);
                return false;
            }

            // Mark as used and save
            storedCode.setUsed(true);
            twoFactorCodeRepository.save(storedCode);

            logger.info("O código multifator foi verificado para o email " + email);
            return true;

        } catch (Exception e) {
            logger.error("Error verifying 2FA code for email: {}", email, e);
            return false;
        }
    }

    @Override
    @Scheduled(fixedRate = 300000) // Clean up every 5 minutes
    public void cleanupExpiredCodes() {
        try {
            LocalDateTime cutoff = LocalDateTime.now().minusMinutes(appConfig.getTwofa().getCodeExpiryMinutes());
            twoFactorCodeRepository.deleteByExpiresAtBefore(cutoff);
            logger.info("Foram limpos os códigos multifator");
        } catch (Exception e) {
            logger.error("Erro ao limpar os codigos multifator");
        }
    }
}
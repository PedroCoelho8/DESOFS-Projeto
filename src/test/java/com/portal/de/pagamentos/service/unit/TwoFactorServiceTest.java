package com.portal.de.pagamentos.service.unit;

import com.portal.de.pagamentos.config.AppConfig;
import com.portal.de.pagamentos.domain.login.TwoFactorCode;
import com.portal.de.pagamentos.repositories.irepositories.ITwoFactorRepository;
import com.portal.de.pagamentos.service.EmailService;
import com.portal.de.pagamentos.service.TwoFactorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TwoFactorServiceTest {

    @Mock
    private ITwoFactorRepository twoFactorCodeRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private AppConfig appConfig;

    private AppConfig.TwoFA twofaConfig;

    @InjectMocks
    private TwoFactorService twoFactorService;

    private final String testEmail = "test@example.com";
    private final String testCode = "123456";
    private final int codeExpiryMinutes = 10;
    private final int maxAttempts = 3;

    @BeforeEach
    void setUp() {
        twofaConfig = mock(AppConfig.TwoFA.class);
    }

    @Test
    void generateAndSendCode_ShouldGenerateCodeAndSendEmail() {
        when(appConfig.getTwofa()).thenReturn(twofaConfig);
        when(twofaConfig.getCodeExpiryMinutes()).thenReturn(codeExpiryMinutes);

        doNothing().when(twoFactorCodeRepository).deleteByEmail(testEmail);
        when(twoFactorCodeRepository.save(any(TwoFactorCode.class))).thenReturn(null);
        doNothing().when(emailService).sendTwoFactorCode(eq(testEmail), anyString());

        assertDoesNotThrow(() -> twoFactorService.generateAndSendCode(testEmail));

        verify(twoFactorCodeRepository).deleteByEmail(testEmail);
        verify(twoFactorCodeRepository).save(any(TwoFactorCode.class));
        verify(emailService).sendTwoFactorCode(eq(testEmail), anyString());
    }

    @Test
    void generateAndSendCode_ShouldThrowRuntimeException_OnRepositoryError() {
        doThrow(new RuntimeException("DB error")).when(twoFactorCodeRepository).deleteByEmail(testEmail);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> twoFactorService.generateAndSendCode(testEmail));

        assertTrue(exception.getMessage().contains("Erro ao gerar e enviar"));
        verify(emailService, never()).sendTwoFactorCode(any(), any());
    }

    @Test
    void verifyCode_ShouldReturnTrue_WhenCodeValid() {
        when(appConfig.getTwofa()).thenReturn(twofaConfig);
        when(twofaConfig.getMaxAttempts()).thenReturn(maxAttempts);

        TwoFactorCode code = new TwoFactorCode(testEmail, testCode, codeExpiryMinutes);
        when(twoFactorCodeRepository.findByEmailAndUsedFalse(testEmail)).thenReturn(Optional.of(code));

        boolean result = twoFactorService.verifyCode(testEmail, testCode);

        assertTrue(result);
        assertTrue(code.isUsed());
        verify(twoFactorCodeRepository).save(code);
    }

    @Test
    void verifyCode_ShouldReturnFalse_WhenCodeNotFound() {
        when(twoFactorCodeRepository.findByEmailAndUsedFalse(testEmail)).thenReturn(Optional.empty());

        boolean result = twoFactorService.verifyCode(testEmail, testCode);

        assertFalse(result);
    }

    @Test
    void verifyCode_ShouldReturnFalse_WhenCodeExpired() {
        TwoFactorCode code = new TwoFactorCode(testEmail, testCode, -1); // expired
        when(twoFactorCodeRepository.findByEmailAndUsedFalse(testEmail)).thenReturn(Optional.of(code));

        boolean result = twoFactorService.verifyCode(testEmail, testCode);

        assertFalse(result);
        verify(twoFactorCodeRepository).delete(code);
    }

    @Test
    void verifyCode_ShouldReturnFalse_WhenMaxAttemptsExceeded() {
        when(appConfig.getTwofa()).thenReturn(twofaConfig);
        when(twofaConfig.getMaxAttempts()).thenReturn(maxAttempts);

        TwoFactorCode code = new TwoFactorCode(testEmail, testCode, codeExpiryMinutes);
        for (int i = 0; i < maxAttempts; i++) {
            code.incrementAttempts();
        }
        when(twoFactorCodeRepository.findByEmailAndUsedFalse(testEmail)).thenReturn(Optional.of(code));

        boolean result = twoFactorService.verifyCode(testEmail, testCode);

        assertFalse(result);
        verify(twoFactorCodeRepository).delete(code);
    }

    @Test
    void verifyCode_ShouldReturnFalse_WhenCodeDoesNotMatch() {
        when(appConfig.getTwofa()).thenReturn(twofaConfig);
        when(twofaConfig.getMaxAttempts()).thenReturn(maxAttempts);

        TwoFactorCode code = new TwoFactorCode(testEmail, testCode, codeExpiryMinutes);
        when(twoFactorCodeRepository.findByEmailAndUsedFalse(testEmail)).thenReturn(Optional.of(code));

        boolean result = twoFactorService.verifyCode(testEmail, "wrongCode");

        assertFalse(result);
        assertEquals(1, code.getAttempts());
        verify(twoFactorCodeRepository).save(code);
    }


    @Test
    void cleanupExpiredCodes_ShouldHandleExceptions() {
        when(appConfig.getTwofa()).thenReturn(twofaConfig);
        when(twofaConfig.getCodeExpiryMinutes()).thenReturn(codeExpiryMinutes);

        doThrow(new RuntimeException("fail")).when(twoFactorCodeRepository).deleteByExpiresAtBefore(any());

        assertDoesNotThrow(() -> twoFactorService.cleanupExpiredCodes());
    }
}

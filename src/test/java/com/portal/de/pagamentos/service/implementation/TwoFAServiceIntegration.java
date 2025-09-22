package com.portal.de.pagamentos.service.implementation;

import com.portal.de.pagamentos.config.AppConfig;
import com.portal.de.pagamentos.domain.login.TwoFactorCode;
import com.portal.de.pagamentos.repositories.irepositories.ITwoFactorRepository;
import com.portal.de.pagamentos.service.EmailService;
import com.portal.de.pagamentos.service.TwoFactorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TwoFAServiceIntegration {

    @Autowired
    private TwoFactorService twoFactorService;

    @Autowired
    private ITwoFactorRepository twoFactorCodeRepository;

    @MockBean
    private EmailService emailService;

    @Autowired
    private AppConfig appConfig;

    private final String testEmail = "integration.test@example.com";
    private final String testEmail2 = "integration.test2@example.com";

    @BeforeEach
    void setUp() {
        // Clean up any existing test data
        twoFactorCodeRepository.deleteByEmail(testEmail);
        twoFactorCodeRepository.deleteByEmail(testEmail2);

        // Configure mock email service
        doNothing().when(emailService).sendTwoFactorCode(anyString(), anyString());
    }

    @Test
    void generateAndSendCode_ShouldPersistCodeInDatabase_WhenCalled() {
        // When
        twoFactorService.generateAndSendCode(testEmail);

        // Then
        Optional<TwoFactorCode> savedCode = twoFactorCodeRepository.findByEmailAndUsedFalse(testEmail);
        assertTrue(savedCode.isPresent());
        assertEquals(testEmail, savedCode.get().getEmail());
        assertEquals(6, savedCode.get().getCode().length());
        assertFalse(savedCode.get().isUsed());
        assertEquals(0, savedCode.get().getAttempts());
        verify(emailService).sendTwoFactorCode(eq(testEmail), anyString());
    }


    /* Mailhog dependent
    @Test
    void generateAndSendCode_ShouldReplaceExistingCode_WhenCalledMultipleTimes() {
        // Given - Generate first code
        twoFactorService.generateAndSendCode(testEmail);
        Optional<TwoFactorCode> firstCode = twoFactorCodeRepository.findByEmailAndUsedFalse(testEmail);
        assertTrue(firstCode.isPresent());
        String firstCodeValue = firstCode.get().getCode();

        // When - Generate second code
        twoFactorService.generateAndSendCode(testEmail);

        // Then
        Optional<TwoFactorCode> secondCode = twoFactorCodeRepository.findByEmailAndUsedFalse(testEmail);
        assertTrue(secondCode.isPresent());
        String secondCodeValue = secondCode.get().getCode();

        // Codes should be different (extremely high probability)
        assertNotEquals(firstCodeValue, secondCodeValue);

        // Should only have one code for the email
        List<TwoFactorCode> allCodes = twoFactorCodeRepository.findAll();
        long codesForEmail = allCodes.stream()
                .filter(code -> code.getEmail().equals(testEmail))
                .count();
        assertEquals(1, codesForEmail);
    }
*/
    @Test
    void verifyCode_ShouldReturnTrueAndMarkAsUsed_WhenCodeIsCorrect() {
        // Given
        twoFactorService.generateAndSendCode(testEmail);
        Optional<TwoFactorCode> savedCode = twoFactorCodeRepository.findByEmailAndUsedFalse(testEmail);
        assertTrue(savedCode.isPresent());
        String generatedCode = savedCode.get().getCode();

        // When
        boolean result = twoFactorService.verifyCode(testEmail, generatedCode);

        // Then
        assertTrue(result);

        // Check that code is marked as used
        Optional<TwoFactorCode> usedCode = twoFactorCodeRepository.findByEmailAndUsedFalse(testEmail);
        assertTrue(usedCode.isEmpty()); // Should not find unused code

        // Verify the code is actually marked as used in database
        Optional<TwoFactorCode> codeInDb = twoFactorCodeRepository.findByEmail(testEmail);
        assertTrue(codeInDb.isPresent());
        assertTrue(codeInDb.get().isUsed());
    }

    @Test
    void verifyCode_ShouldIncrementAttempts_WhenCodeIsIncorrect() {
        // Given
        twoFactorService.generateAndSendCode(testEmail);
        String wrongCode = "000000";

        // When
        boolean result = twoFactorService.verifyCode(testEmail, wrongCode);

        // Then
        assertFalse(result);

        Optional<TwoFactorCode> codeAfterAttempt = twoFactorCodeRepository.findByEmailAndUsedFalse(testEmail);
        assertTrue(codeAfterAttempt.isPresent());
        assertEquals(1, codeAfterAttempt.get().getAttempts());
        assertFalse(codeAfterAttempt.get().isUsed());
    }

    @Test
    void verifyCode_ShouldDeleteCode_WhenMaxAttemptsReached() {
        // Given
        twoFactorService.generateAndSendCode(testEmail);
        String wrongCode = "000000";
        int maxAttempts = appConfig.getTwofa().getMaxAttempts();

        // When - Exhaust all attempts
        for (int i = 0; i < maxAttempts; i++) {
            boolean result = twoFactorService.verifyCode(testEmail, wrongCode);
            assertFalse(result);
        }

        // Try one more time after max attempts reached
        boolean finalResult = twoFactorService.verifyCode(testEmail, wrongCode);

        // Then
        assertFalse(finalResult);
        Optional<TwoFactorCode> codeAfterMaxAttempts = twoFactorCodeRepository.findByEmailAndUsedFalse(testEmail);
        assertTrue(codeAfterMaxAttempts.isEmpty());
    }

    @Test
    void verifyCode_ShouldHandleExpiredCode_ByDeletingIt() {
        // Given - Create an expired code manually
        TwoFactorCode expiredCode = new TwoFactorCode(
                testEmail,
                "123456",
                -1 // Negative expiry minutes to make it expired immediately
        );
        twoFactorCodeRepository.save(expiredCode);

        // When
        boolean result = twoFactorService.verifyCode(testEmail, "123456");

        // Then
        assertFalse(result);
        Optional<TwoFactorCode> codeAfterVerification = twoFactorCodeRepository.findByEmailAndUsedFalse(testEmail);
        assertTrue(codeAfterVerification.isEmpty());
    }


    @Test
    void generateAndSendCode_ShouldGenerateNumericCode_WithCorrectLength() {
        // When
        twoFactorService.generateAndSendCode(testEmail);

        // Then
        Optional<TwoFactorCode> savedCode = twoFactorCodeRepository.findByEmailAndUsedFalse(testEmail);
        assertTrue(savedCode.isPresent());
        String code = savedCode.get().getCode();

        assertEquals(6, code.length());
        assertTrue(code.matches("\\d{6}")); // Should be 6 digits
        assertTrue(Integer.parseInt(code) >= 0);
        assertTrue(Integer.parseInt(code) <= 999999);
    }

    @Test
    void verifyCode_ShouldReturnFalse_WhenNoCodeExists() {
        // When - Try to verify code for email that never had a code generated
        boolean result = twoFactorService.verifyCode("nonexistent@test.com", "123456");

        // Then
        assertFalse(result);
    }
}
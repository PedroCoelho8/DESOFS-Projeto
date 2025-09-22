package com.portal.de.pagamentos.service.unit;

class UserServiceTest {

/** SUSPENDED TESTS -> UNTIL OAUTH IS DEVELOPED
import com.portal.de.pagamentos.config.JwtTokenProvider;
import com.portal.de.pagamentos.controller.mapper.UserMapper;
import com.portal.de.pagamentos.domain.Login.LoginAttempt;
import com.portal.de.pagamentos.domain.Login.DTO.LoginResponseDTO;
import com.portal.de.pagamentos.domain.user.DTO.UserDTO;
import com.portal.de.pagamentos.domain.user.User;
import com.portal.de.pagamentos.repositories.IRepositories.IUserRepository;
import com.portal.de.pagamentos.repositories.IRepositories.LoginAttemptRepository;
import com.portal.de.pagamentos.service.TwoFactorService;
import com.portal.de.pagamentos.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.*;
import org.springframework.security.authentication.BadCredentialsException;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {
    private UserService userService;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private LoginAttemptRepository loginAttemptRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserMapper userMapper;

    @Mock
    private TwoFactorService twoFactorService;

    @Captor
    ArgumentCaptor<LoginAttempt> loginAttemptCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(
                userRepository,
                loginAttemptRepository,
                userMapper,
                mock(org.springframework.security.authentication.AuthenticationManager.class),
                jwtTokenProvider,
                twoFactorService
        );
    }

    @Test
    @DisplayName("Should create a new user")
    void shouldCreateUser() {
        UserDTO dto = new UserDTO();
        dto.setName("João Silva");
        dto.setEmail("joao@example.com");
        dto.setPassword("MySecurePass123!");
        dto.setRoles(Arrays.asList("ADMIN", "USER"));
        dto.setPhoneNumber("912345678");
        dto.setNif("123456789");

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());

        when(userMapper.toDTO(any(User.class))).thenReturn(dto);
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO result = userService.createUser(dto);

        assertNotNull(result);
        assertEquals(dto.getEmail(), result.getEmail());
    }

    @Test
    @DisplayName("Should login successfully")
    void login_successful() {
        String email = "user@example.com";
        String rawPassword = "correct_password";
        String hashedPassword = BCrypt.hashpw(rawPassword, BCrypt.gensalt());

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName("Joaozinho");
        user.setEmail(email);
        user.setPassword(hashedPassword);
        user.setRoles(Collections.singletonList("ROLE_USER"));
        user.setNif("123456789");
        user.setPhoneNumber("912345678");

        LoginAttempt attempt = new LoginAttempt(email);
        attempt.setAttempts();
        attempt.setLastAttempt(LocalDateTime.now().minusMinutes(10));

        when(loginAttemptRepository.findById(email)).thenReturn(Optional.of(attempt));
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(jwtTokenProvider.generateToken(any())).thenReturn("fake-jwt-token");

        UserDTO dto = new UserDTO();
        dto.setEmail(email);
        when(userMapper.toDTO(user)).thenReturn(dto);

        LoginResponseDTO response = userService.login(email, rawPassword);

        assertNotNull(response);
        assertEquals("fake-jwt-token", response.getToken());
        assertEquals(email, response.getUserDTO().getEmail());

        verify(loginAttemptRepository).deleteById(email);
    }

    @Test
    @DisplayName("Should fail login when email is not found")
    void shouldFailLoginForUnknownEmail() {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userService.login("missing@example.com", "pass")
        );

        assertEquals("Utilizador não encontrado", exception.getMessage());
    }

    @Test
    @DisplayName("Should fail login when password is incorrect")
    void shouldFailLoginWithWrongPassword() {
        String email = "user@example.com";
        String correctPassword = "correct";
        String wrongPassword = "wrong";

        User user = new User();
        user.setEmail(email);
        user.setPassword(BCrypt.hashpw(correctPassword, BCrypt.gensalt()));

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(loginAttemptRepository.findById(email)).thenReturn(Optional.of(new LoginAttempt(email)));

        when(userService.login(email, wrongPassword))
                .thenThrow(new BadCredentialsException("Credenciais inválidas"));

        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () ->
                userService.login(email, wrongPassword)
        );

        assertEquals("Credenciais inválidas", exception.getMessage());
    }

    /*
    @Test
    @DisplayName("Should block login after too many failed attempts")
    void shouldBlockLoginAfterTooManyAttempts() {
        String email = "blocked@example.com";

        User user = new User();
        user.setEmail(email);
        user.setPassword(BCrypt.hashpw("whatever", BCrypt.gensalt()));

        LoginAttempt attempt = new LoginAttempt(email);
        attempt.setAttempts(100);
        attempt.setLastAttempt(LocalDateTime.now());

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(loginAttemptRepository.findById(email)).thenReturn(Optional.of(attempt));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            userService.login(email, "password")
        );

        assertEquals("Muitas tentativas falhadas. Tente novamente mais tarde", exception.getMessage());
    }
    */
}

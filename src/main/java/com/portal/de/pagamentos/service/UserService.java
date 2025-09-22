package com.portal.de.pagamentos.service;

import com.portal.de.pagamentos.config.JwtTokenProvider;
import com.portal.de.pagamentos.controller.mapper.UserMapper;
import com.portal.de.pagamentos.domain.login.LoginAttempt;
import com.portal.de.pagamentos.domain.login.dto.LoginResponseDTO;
import com.portal.de.pagamentos.domain.user.dto.UserDTO;
import com.portal.de.pagamentos.domain.user.User;
import com.portal.de.pagamentos.exceptions.PhotoUploadException;
import com.portal.de.pagamentos.repositories.irepositories.IUserRepository;
import com.portal.de.pagamentos.repositories.irepositories.LoginAttemptRepository;
import com.portal.de.pagamentos.service.iservices.IUserService;
import com.portal.de.pagamentos.service.factory.UserFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {

    private final IUserRepository userRepository;
    private final LoginAttemptRepository loginAttemptRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final TwoFactorService twoFactorService;
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(IUserRepository userRepository,
                       LoginAttemptRepository loginAttemptRepository,
                       UserMapper userMapper,
                       AuthenticationManager authenticationManager,
                       JwtTokenProvider jwtTokenProvider,
                       TwoFactorService twoFactorService) {
        this.userRepository = userRepository;
        this.loginAttemptRepository = loginAttemptRepository;
        this.userMapper = userMapper;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.twoFactorService = twoFactorService;
    }

    public UserDTO createUser(UserDTO userDTO, MultipartFile photo) throws PhotoUploadException {
        UserFactory userFactory = new UserFactory();

        if(userRepository.existsByEmail(userDTO.getEmail())) {
            logger.error("This email already exists");
            throw new IllegalArgumentException("This email already exists in another user's account");
        }

        if(userRepository.existsByNif(userDTO.getNif())){
            logger.error("This nif already exists");
            throw new IllegalArgumentException("This NIF already exists in another user's account");
        }

        if (photo != null && !photo.isEmpty()) {
            userDTO = storeUserPhoto(photo, userDTO);
        }

        User user = userFactory.createUser(userDTO);
        userRepository.save(user);

        logger.info("User was successfully created");

        return userMapper.toDTO(user);
    }

    private UserDTO storeUserPhoto(MultipartFile photo, UserDTO userDTO) throws PhotoUploadException {
        try {
            if (!Objects.requireNonNull(photo.getContentType()).startsWith("image/")) {
                throw new IllegalArgumentException("Only image files are allowed");
            }

            String userId = UUID.randomUUID().toString();

            if (userId == null || userId.trim().isEmpty()) {
                logger.error("UserId is null or empty!");
                throw new IllegalArgumentException("UserId cannot be null or empty");
            }

            Path baseDir = Paths.get("user-uploads").toAbsolutePath().normalize();
            Path userDir = baseDir.resolve(userId).normalize();

            logger.info("3");
            if (!userDir.startsWith(baseDir)) {
                throw new SecurityException("Invalid storage path");
            }

            logger.info("4");
            Files.createDirectories(userDir);

            String safeFilename = UUID.randomUUID() + "-" + photo.getOriginalFilename().replaceAll("[^a-zA-Z0-9\\.\\-_]", "");
            Path photoPath = userDir.resolve(safeFilename);

            try (InputStream is = photo.getInputStream()) {
                Files.copy(is, photoPath, StandardCopyOption.REPLACE_EXISTING);
            }

            userDTO.setId(userId);
            userDTO.setPhotoPath(userId + "/" + safeFilename);

            return userDTO;

        } catch (IOException e) {
            logger.error("Não foi possível guardar a foto");
            throw new PhotoUploadException("Could not store photo", e);
        }
    }

    @Override
    public LoginResponseDTO login(String email, String password) {
        // Check rate limiting - Fixed to use findByEmail instead of findById
        Optional<LoginAttempt> attemptOpt = loginAttemptRepository.findByEmail(email);
        LoginAttempt attempt = attemptOpt.orElse(new LoginAttempt(email));

        // Reset attempts if more than 1 hour has passed since last attempt
        if (attempt.getLastAttempt() != null &&
                attempt.getLastAttempt().isBefore(LocalDateTime.now().minusHours(1))) {
            logger.info("A última tentativa foi feita há mais de 1 hora");
            attempt.resetAttempts();
        }

        // Check if user is currently rate limited
        if (attempt.getLastAttempt() != null &&
                attempt.getLastAttempt().isAfter(LocalDateTime.now().minusHours(1)) &&
                attempt.getAttempts() >= 100) {
            logger.error("Muitas tentativas falhadas. Tente novamente mais tarde");
            throw new IllegalArgumentException("Muitas tentativas falhadas. Tente novamente mais tarde");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            // Clear failed attempts on successful authentication - Fixed to use deleteByEmail
            loginAttemptRepository.deleteByEmail(email);

            // Generate and send 2FA code
            twoFactorService.generateAndSendCode(email);

            logger.info("O código de duplo fator foi enviado para o email " + email);

            // Return response indicating 2FA is required
            return new LoginResponseDTO(true, "O código de duplo fator foi enviado para o seu email");

        } catch (AuthenticationException e) {
            attempt.incrementAttempts();
            logger.error("Credenciais inválidas. Autenticação falhou pela "+ attempt.getAttempts()+ "a vez, tente outra vez");
            attempt.setLastAttempt(LocalDateTime.now());
            loginAttemptRepository.save(attempt); // This will work now since attempt has proper ID handling

            throw new IllegalArgumentException("Credenciais inválidas");
        }
    }

    public LoginResponseDTO verifyTwoFactorCode(String email, String code) {
        if (!twoFactorService.verifyCode(email, code)) {
            throw new IllegalArgumentException("Invalid or expired verification code");
        }

        Optional<User> optUser = userRepository.findByEmail(email);

        if(optUser.isEmpty()){
            logger.error("O email não está no sistema");
            throw new IllegalArgumentException("O email não está no sistema");
        }else{
            User user = optUser.get();

            // Use Spring Security's built-in User class (required by your JwtTokenProvider)
            Collection<GrantedAuthority> authorities = user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                    .collect(Collectors.toList());

            UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                    .username(user.getEmail().getValue())
                    .password(user.getPassword().getValue())
                    .authorities(authorities)
                    .accountExpired(false)
                    .accountLocked(false)
                    .credentialsExpired(false)
                    .disabled(false)
                    .build();

            // Create authentication with UserDetails
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );

            String token = jwtTokenProvider.generateToken(authentication);

            logger.info("O utilizador com email " + user.getEmail().getValue() + " iniciou sessão com sucesso");

            return new LoginResponseDTO(token, userMapper.toDTO(user));
        }
    }
}
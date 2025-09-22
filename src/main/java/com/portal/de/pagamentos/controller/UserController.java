package com.portal.de.pagamentos.controller;

import com.portal.de.pagamentos.controller.icontroller.IUserController;
import com.portal.de.pagamentos.domain.login.dto.LoginRequestDTO;
import com.portal.de.pagamentos.domain.login.dto.LoginResponseDTO;
import com.portal.de.pagamentos.domain.login.dto.TwoFactorVerificationDTO;
import com.portal.de.pagamentos.domain.user.dto.UserDTO;
import com.portal.de.pagamentos.exceptions.PhotoUploadException;
import com.portal.de.pagamentos.service.iservices.IUserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController implements IUserController {
    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDTO> createUser(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("nif") String nif,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam(value = "roles", required = false) List<String> roles,
            @RequestPart(value = "photo", required = false) MultipartFile photo) throws PhotoUploadException {

        UserDTO userDTO = new UserDTO();
        userDTO.setName(name);
        userDTO.setEmail(email);
        userDTO.setPassword(password);
        userDTO.setNif(nif);
        userDTO.setPhoneNumber(phoneNumber);
        userDTO.setRoles(roles);

        UserDTO createdUser = userService.createUser(userDTO, photo);
        return ResponseEntity.ok(createdUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        LoginResponseDTO response = userService.login(loginRequest.getEmail(), loginRequest.getPassword());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-2fa")
    public ResponseEntity<LoginResponseDTO> verifyTwoFactor(@RequestBody TwoFactorVerificationDTO verificationDTO) {
        LoginResponseDTO response = userService.verifyTwoFactorCode(
                verificationDTO.getEmail(),
                verificationDTO.getCode()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/teste/token")
    public String validateToken() {
        return "Token Successfully validated.";
    }
}


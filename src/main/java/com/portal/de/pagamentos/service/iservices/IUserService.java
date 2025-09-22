package com.portal.de.pagamentos.service.iservices;

import com.portal.de.pagamentos.domain.login.dto.LoginResponseDTO;
import com.portal.de.pagamentos.domain.user.dto.UserDTO;
import com.portal.de.pagamentos.exceptions.PhotoUploadException;
import org.springframework.web.multipart.MultipartFile;

public interface IUserService {

    LoginResponseDTO login(String email, String password);

    UserDTO createUser(UserDTO userDTO, MultipartFile photo) throws PhotoUploadException;

    LoginResponseDTO verifyTwoFactorCode(String email, String code);
}

package com.portal.de.pagamentos.service.factory;

import com.portal.de.pagamentos.domain.user.dto.UserDTO;
import com.portal.de.pagamentos.domain.user.User;

public class UserFactory {


    public UserFactory(){}

    public User createUser(UserDTO userDTO) {

        User user = new User();

        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setNif(userDTO.getNif());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setRoles(userDTO.getRoles());
        user.setPhotoPath(userDTO.getPhotoPath());

        return user;
    }
}

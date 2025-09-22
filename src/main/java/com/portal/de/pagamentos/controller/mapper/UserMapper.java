package com.portal.de.pagamentos.controller.mapper;

import com.portal.de.pagamentos.domain.user.dto.UserDTO;
import com.portal.de.pagamentos.domain.user.Role;
import com.portal.de.pagamentos.domain.user.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserMapper(){
        super();
    }

    public User toDomain(UserDTO userDTO){
        User user = new User();

        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setNif(userDTO.getNif());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setPhotoPath(userDTO.getPhotoPath());

        return user;
    }

    public UserDTO toDTO(User user){
        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId().toString());
        userDTO.setName(user.getName().toString());
        userDTO.setEmail(user.getEmail().toString());
        userDTO.setNif(user.getNif().toString());
        userDTO.setPhoneNumber(user.getPhoneNumber().toString());
        userDTO.setPhotoPath(user.getPhotoPath());

        userDTO.setRoles(user.getRoles().stream()
                .map(Role::name)
                .toList());

        return userDTO;
    }
}

package com.portal.de.pagamentos.domain.user.dto;

import java.util.List;

public class UserDTO {
    private String id;

    private String name;

    private String email;

    private String password;

    private List<String> roles;

    private String phoneNumber;

    private String nif;

    private String photoPath;

    public UserDTO() {}

    public UserDTO(String name, String email, String password, List<String> roles, String phoneNumber, String nif, String photo){
        setName(name);
        setEmail(email);
        setPassword(password);
        setRoles(roles);
        setPhoneNumber(phoneNumber);
        setNif(nif);
        setPhotoPath(photo);
    }

    public String getId(){ return this.id; }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public List<String> getRoles() {
        return this.roles;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public String getNif() {
        return this.nif;
    }

    public String getPhotoPath(){
        return this.photoPath;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber =  phoneNumber;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }
}

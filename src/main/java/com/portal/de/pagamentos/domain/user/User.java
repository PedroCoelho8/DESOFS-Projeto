package com.portal.de.pagamentos.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.portal.de.pagamentos.domain.NIF;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Entity
@Table(name ="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Embedded
    private Username name;

    @Embedded
    private Email email;

    @Embedded
    @JsonIgnore
    private Password password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private List<Role> roles;

    @Embedded
    private PhoneNumber phoneNumber;

    @Embedded
    private NIF nif;

    @Column(name = "photo_path")
    private String photoPath;

    public User() {}

    public User(String name, String email, String password, List<String> roles, String phoneNumber, String nif, String photoPath) {
        setName(name);
        setEmail(email);
        setPassword(password);
        setRoles(roles);
        setPhoneNumber(phoneNumber);
        setNif(nif);
        setPhotoPath(photoPath);
    }

    public UUID getId(){ return this.id; }

    public Username getName() {
        return this.name;
    }

    public Email getEmail() {
        return this.email;
    }

    public Password getPassword() {
        return this.password;
    }

    public List<Role> getRoles() {
        return this.roles;
    }

    public PhoneNumber getPhoneNumber() {
        return this.phoneNumber;
    }

    public NIF getNif() {
        return this.nif;
    }

    public String getPhotoPath() {
        return this.photoPath;
    }

    public void setName(String name) {
        this.name = new Username(name);
    }

    public void setEmail(String email) {
        this.email = new Email(email);
    }

    public void setPassword(String password) {
        this.password = new Password(password);
    }

    public void setRoles(List<String> roles) {
        List<Role> result = new ArrayList<>();
        for(String role : roles){
            Optional<Role> opt = Role.findByCode(role);
            opt.ifPresent(result::add);
        }
        if(result.isEmpty()){
            result.add(Role.GUEST);
        }
        this.roles = result;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = new PhoneNumber(phoneNumber);
    }

    public void setNif(String nif) {
        this.nif = new NIF(nif);
    }

    public void setId(UUID uuid) {
        this.id= uuid;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }
}

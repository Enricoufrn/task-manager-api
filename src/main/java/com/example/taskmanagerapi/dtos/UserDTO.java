package com.example.taskmanagerapi.dtos;

import com.example.taskmanagerapi.domain.User;
import com.example.taskmanagerapi.domain.enumerations.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class UserDTO extends AbstractDTO<User> {
    private UUID id;
    @NotBlank(message = "Login é obrigatório")
    private String login;
    @NotBlank(message = "Senha é obrigatória")
    private String password;
    @NotBlank(message = "Nome é obrigatório")
    private String name;
    @NotBlank(message = "Email é obrigatório")
    private String email;
    @NotNull(message = "Role é obrigatório")
    private String role;
    public UserDTO() {
    }
    public UserDTO(User user){
        this.id = user.getId();
        this.login = user.getLogin();
        this.email = user.getEmail();
        this.name = user.getName();
        this.password = "********";
        this.role = user.getRole().name();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public User toEntity() {
        User user = new User();
        user.setId(this.id);
        user.setLogin(this.login);
        user.setPassword(this.password);
        user.setName(this.name);
        user.setEmail(this.email);
        if (this.role != null && !this.role.isEmpty())
            user.setRole(Role.valueOf(this.role));
        return user;
    }

    @Override
    public AbstractDTO<User> fromEntity(User entity) {
        return new UserDTO(entity);
    }
}

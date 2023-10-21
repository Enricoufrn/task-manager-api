package com.example.taskmanagerapi.domain.enumerations;

public enum Role {
    ROLE_ADMIN("Administrador"),
    ROLE_COMMON("Usuário comum")
    ;
    private String description;
    Role(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
}

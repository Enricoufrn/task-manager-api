package com.example.taskmanagerapi.domain.enumerations;

public enum TaskStatus {
    TODO("Não iniciada"),
    DOING("Em progresso"),
    DONE("Finalizada"),
    ARCHIVED("Arquivada")
    ;

    private String description;
    TaskStatus(String description){
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
}

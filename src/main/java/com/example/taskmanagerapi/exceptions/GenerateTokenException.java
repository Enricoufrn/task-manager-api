package com.example.taskmanagerapi.exceptions;

public class GenerateTokenException extends GenericCustomException {
    public GenerateTokenException(String message) {
        super(message);
    }

    public GenerateTokenException(String message, String details) {
        super(message, details);
    }
}

package com.example.taskmanagerapi.exceptions;

public abstract class GenericCustomException extends RuntimeException{
    private String message;
    private String details;
    public GenericCustomException(String message) {
        super(message);
        this.message = message;
        this.details = "";
    }
    public GenericCustomException(String message, String details) {
        super(message);
        this.message = message;
        this.details = details;
    }
    public String getMessage() {
        return message;
    }
    public String getDetails() {
        return details;
    }
}

package com.example.taskmanagerapi.exceptions;

import org.springframework.http.HttpStatus;

public class ResourceException extends GenericCustomException{
    private HttpStatus httpStatus;

    public ResourceException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public ResourceException(String message, String details, HttpStatus httpStatus) {
        super(message, details);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}

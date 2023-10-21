package com.example.taskmanagerapi.exceptions;

import jakarta.servlet.ServletException;
import org.springframework.http.HttpStatus;

public class CustomAuthorizationException extends ServletException {
    private String message;
    private String details;
    private HttpStatus httpStatus = HttpStatus.FORBIDDEN;
    public CustomAuthorizationException(String msg) {
        super(msg);
        this.message = msg;
        this.details = "";
    }

    public CustomAuthorizationException(String message, String details) {
        super(message);
        this.message = message;
        this.details = details;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}

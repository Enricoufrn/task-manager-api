package com.example.taskmanagerapi.exceptions;

public class ExtractTokenClaimException extends GenericCustomException{
    public ExtractTokenClaimException(String message) {
        super(message);
    }

    public ExtractTokenClaimException(String message, String details) {
        super(message, details);
    }
}

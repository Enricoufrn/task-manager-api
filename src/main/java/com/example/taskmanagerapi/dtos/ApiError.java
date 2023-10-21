package com.example.taskmanagerapi.dtos;

import java.util.Date;

/**
 * DTO to represent the error response
 * @param timestamp
 * @param status
 * @param message
 * @param details
 */
public record ApiError(Long timestamp, String status, String message, String details) {
    public ApiError(String status, String message, String details) {
        this(new Date().getTime(), status, message, details);
    }
}

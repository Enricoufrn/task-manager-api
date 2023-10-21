package com.example.taskmanagerapi.services;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * Service for jwt token operations interface
 */
public interface IJwtService {
    /**
     * Extracts login from token
     * @param token jwt token
     * @return login
     */
    String extractLogin(String token);

    /**
     * Generates new jwt token
     * @param userDetails user details object
     * @return jwt token
     */
    String generateToken(UserDetails userDetails);

    /**
     * Checks if token is valid. If the user is the same and token is not expired
     * @param token jwt token
     * @param userDetails user details object
     * @return  true if token is valid, false otherwise
     */
    boolean isValidToken(String token, UserDetails userDetails);
}

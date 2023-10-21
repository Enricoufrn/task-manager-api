package com.example.taskmanagerapi.services;

import com.example.taskmanagerapi.exceptions.GenerateTokenException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Interface to be implemented by the authentication service
 */
public interface IAuthenticationService {
    Authentication authenticate(String login, String password) throws ServletException;
    void addJwtTokenToHeader(UserDetails userDetails, HttpServletResponse response) throws GenerateTokenException;
}

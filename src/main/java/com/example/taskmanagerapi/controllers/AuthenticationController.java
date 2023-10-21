package com.example.taskmanagerapi.controllers;

import com.example.taskmanagerapi.domain.User;
import com.example.taskmanagerapi.dtos.AuthenticationRequestDTO;
import com.example.taskmanagerapi.dtos.UserDTO;
import com.example.taskmanagerapi.services.IAuthenticationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")
public class AuthenticationController {
    private final IAuthenticationService authenticationService;
    private final UserDetailsService userDetailsService;

    public AuthenticationController(IAuthenticationService authenticationService, UserDetailsService userDetailsService) {
        this.authenticationService = authenticationService;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequestDTO authenticationRequestDTO, HttpServletResponse response) throws ServletException {
        Authentication authentication = this.authenticationService.authenticate(authenticationRequestDTO.login(), authenticationRequestDTO.password());
        UserDTO userDTO = new UserDTO((User) authentication.getPrincipal());
        this.authenticationService.addJwtTokenToHeader(this.userDetailsService.loadUserByUsername(userDTO.getLogin()), response);
        return ResponseEntity.ok().body(userDTO);
    }
}

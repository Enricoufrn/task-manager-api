package com.example.taskmanagerapi.services;

import com.example.taskmanagerapi.domain.enumerations.MessageCode;
import com.example.taskmanagerapi.exceptions.CustomAuthorizationException;
import com.example.taskmanagerapi.helper.MessageHelper;
import com.example.taskmanagerapi.utils.AuthUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthenticationServiceImpl implements IAuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final MessageHelper messageHelper;
    private final IJwtService jwtService;

    public AuthenticationServiceImpl(AuthenticationManager authenticationManager, MessageHelper messageHelper, IJwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.messageHelper = messageHelper;
        this.jwtService = jwtService;
    }

    @Override
    public Authentication authenticate(String login, String password) throws ServletException {
        this.validate(login, password);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(login, password);
        Authentication authentication = this.authenticationManager.authenticate(authToken);
        if (Objects.isNull(authentication) || !authentication.isAuthenticated()) {
            throw new CustomAuthorizationException(this.messageHelper.getMessage(MessageCode.INVALID_CREDENTIALS));
        }else return authentication;
    }

    @Override
    public void addJwtTokenToHeader(UserDetails userDetails, HttpServletResponse response) {
        String token = this.jwtService.generateToken(userDetails);
        response.addHeader(AuthUtils.AUTHORIZATION_PREFIX_HEADER, AuthUtils.TOKEN_IDENTIFICATION_PREFIX + token);
        response.addHeader(AuthUtils.ACCESS_CONTROL_EXPOSE_HEADERS, AuthUtils.AUTHORIZATION_PREFIX_HEADER);
    }

    public void validate(String login, String password) throws CustomAuthorizationException {
        if (Objects.isNull(login) || login.isEmpty()) {
            throw new CustomAuthorizationException(this.messageHelper.getMessage(MessageCode.INVALID_CREDENTIALS),
                    this.messageHelper.getMessage(MessageCode.INVALID_LOGIN));
        }
        if (Objects.isNull(password) || password.isEmpty()) {
            throw new CustomAuthorizationException(this.messageHelper.getMessage(MessageCode.INVALID_CREDENTIALS),
                    this.messageHelper.getMessage(MessageCode.INVALID_PASSWORD));
        }
    }
}

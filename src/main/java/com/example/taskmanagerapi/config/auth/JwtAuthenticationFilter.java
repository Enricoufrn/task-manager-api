package com.example.taskmanagerapi.config.auth;

import com.example.taskmanagerapi.domain.enumerations.MessageCode;
import com.example.taskmanagerapi.exceptions.CustomAuthorizationException;
import com.example.taskmanagerapi.helper.MessageHelper;
import com.example.taskmanagerapi.services.IJwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.example.taskmanagerapi.utils.AuthUtils.*;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private final UserDetailsService userDetailsService;
    private final MessageHelper messageHelper;
    private final IJwtService IJwtService;

    public JwtAuthenticationFilter(UserDetailsService userDetailsService, MessageHelper messageHelper, IJwtService IJwtService) {
        this.messageHelper = messageHelper;
        this.IJwtService = IJwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader(AUTHORIZATION_PREFIX_HEADER);
        if  (request.getServletPath().equals(AUTHENTICATION_URL) || request.getServletPath().equals(NEW_USER_URL)){
            filterChain.doFilter(request, response);
            return;
        }
        if (authorizationHeader == null || !authorizationHeader.startsWith(TOKEN_IDENTIFICATION_PREFIX)){
            throw new CustomAuthorizationException(this.messageHelper.getMessage(MessageCode.AUTHORIZATION_HEADER_NOT_FOUND));
        }else{
            String token = authorizationHeader.replace(TOKEN_IDENTIFICATION_PREFIX, "");
            if (token.isEmpty()){
                throw new CustomAuthorizationException(this.messageHelper.getMessage(MessageCode.INVALID_AUTHORIZATION_HEADER),
                        this.messageHelper.getMessage(MessageCode.TOKEN_NOT_FOUND));
            }else{
                String login = this.IJwtService.extractLogin(token);
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(login);
                if (this.IJwtService.isValidToken(token, userDetails)) {
                    SecurityContext context = SecurityContextHolder.createEmptyContext();
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    context.setAuthentication(authToken);
                    SecurityContextHolder.setContext(context);
                    filterChain.doFilter(request, response);
                }else{
                    throw new CustomAuthorizationException(this.messageHelper.getMessage(MessageCode.INVALID_AUTHORIZATION_HEADER),
                            this.messageHelper.getMessage(MessageCode.INVALID_TOKEN));
                }
            }
        }
    }
}

package com.example.taskmanagerapi.utils;

/**
 * Class to store authentication process constants
 */
public class AuthUtils {
    public static final String AUTHENTICATION_URL = "/api/v1/auth/login";
    public static final String NEW_USER_URL = "/api/v1/user";
    public static final String TOKEN_IDENTIFICATION_PREFIX = "Bearer ";
    public static final String AUTHORIZATION_PREFIX_HEADER = "Authorization";
    public static final String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";
}

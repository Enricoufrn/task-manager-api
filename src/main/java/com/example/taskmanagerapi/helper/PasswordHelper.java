package com.example.taskmanagerapi.helper;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordHelper {
    private final PasswordEncoder encoder;

    public PasswordHelper(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    /**
     * Generates a password using the login and the password as the base of the encryption
     */
    public String generatePassword(String password) {
        return encoder.encode(password);
    }
}

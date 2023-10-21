package com.example.taskmanagerapi.helper;

import com.example.taskmanagerapi.domain.User;
import com.example.taskmanagerapi.domain.enumerations.Role;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserHelper {
    public User getLoggedUser(){
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
    public boolean isLoggedUserAdmin(){
        return this.getLoggedUser().getRole().equals(Role.ROLE_ADMIN);
    }
}

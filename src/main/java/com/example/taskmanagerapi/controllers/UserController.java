package com.example.taskmanagerapi.controllers;

import com.example.taskmanagerapi.dtos.UserDTO;
import com.example.taskmanagerapi.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user")
@CrossOrigin(origins = "*")
public class UserController extends GenericController {
    private final UserService userService;

    public UserController(UserService userService, UriBuilder uriBuilder) {
        super(uriBuilder);
        this.userService = userService;
    }
    @GetMapping
    public ResponseEntity<?> getUsers() {
        List<UserDTO> users = this.userService.findAll();
        return ResponseEntity.ok(users);
    }
    @GetMapping(ID)
    public ResponseEntity<?> getUserById(@PathVariable UUID id) {
        UserDTO user = this.userService.findUserById(id);
        return ResponseEntity.ok(user);
    }
    @PostMapping
    public ResponseEntity<?> saveUser(@RequestBody @Valid UserDTO user) {
        UserDTO saved = this.userService.save(user);
        URI uri = this.uriBuilder.replacePath(this.getByIdPath()).build(saved.getId());
        return ResponseEntity.created(uri).body(saved);
    }
    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody @Valid UserDTO user) {
        UserDTO updated = this.userService.save(user);
        return ResponseEntity.ok(updated);
    }
    @DeleteMapping(ID)
    public ResponseEntity<?> deleteUser(@PathVariable UUID id) {
        this.userService.delete(id);
        return ResponseEntity.ok().build();
    }
}

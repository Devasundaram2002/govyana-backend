package com.deva.Govyana.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.deva.Govyana.dto.LoginRequest;
import com.deva.Govyana.model.User;
import com.deva.Govyana.service.UserService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService;

    // REGISTER
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            userService.register(user);
            return ResponseEntity.ok("Registered successfully");
        } catch (RuntimeException e) {
            if (e.getMessage().equals("EMAIL_EXISTS")) {
                return ResponseEntity.badRequest().body("Email already exists");
            }
            return ResponseEntity.internalServerError().body("Server error");
        }
    }

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            userService.login(request);
            return ResponseEntity.ok("Login successful");
        } catch (RuntimeException e) {

            if (e.getMessage().equals("USER_NOT_FOUND")) {
                return ResponseEntity.status(404).body("User not found");
            }

            if (e.getMessage().equals("INVALID_PASSWORD")) {
                return ResponseEntity.status(401).body("Invalid password");
            }

            return ResponseEntity.internalServerError().body("Server error");
        }
    }
}

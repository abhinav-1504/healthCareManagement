package com.example.healthcare_appointment_api.controller;

import com.example.healthcare_appointment_api.dto.AuthRequest;
import com.example.healthcare_appointment_api.dto.AuthResponse;
import com.example.healthcare_appointment_api.model.User;
import com.example.healthcare_appointment_api.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody User user) {
        return ResponseEntity.ok(userService.register(user));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(userService.login(authRequest));
    }
}
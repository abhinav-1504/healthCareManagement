package com.example.healthcare_appointment_api.service;

import com.example.healthcare_appointment_api.dto.AuthRequest;
import com.example.healthcare_appointment_api.dto.AuthResponse;
import com.example.healthcare_appointment_api.model.User;
import com.example.healthcare_appointment_api.repository.UserRepository;
import com.example.healthcare_appointment_api.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

@Service
public class UserService {
    private static final Logger logger = Logger.getLogger(UserService.class.getName());

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse register(User user) {
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            Set<String> defaultRoles = new HashSet<>();
            defaultRoles.add("ROLE_PATIENT");
            user.setRoles(defaultRoles);
        } else {
            Set<String> normalizedRoles = new HashSet<>();
            for (String role : user.getRoles()) {
                normalizedRoles.add(role.startsWith("ROLE_") ? role.toUpperCase() : "ROLE_" + role.toUpperCase());
            }
            user.setRoles(normalizedRoles);
        }
        logger.info("Registering user: " + user.getUsername() + " with roles: " + user.getRoles());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        String role = extractRole(user);
        String token = jwtUtil.generateToken(user.getUsername(), role);
        return new AuthResponse(token);
    }

    public AuthResponse login(AuthRequest authRequest) {
        logger.info("Attempting login for username: " + authRequest.getUsername());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = userRepository.findByUsername(authRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found: " + authRequest.getUsername()));
        String role = extractRole(user);
        logger.info("User logged in: " + authRequest.getUsername() + " with role: " + role);
        String token = jwtUtil.generateToken(user.getUsername(), role);
        return new AuthResponse(token);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }

    private String extractRole(User user) {
        return user.getRoles().stream()
                .filter(r -> r.startsWith("ROLE_"))
                .map(r -> r.replace("ROLE_", ""))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Role not found for user: " + user.getUsername()));
    }
}
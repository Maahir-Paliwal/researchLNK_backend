package com.maahir.researchLNK_backend.controller;

import com.maahir.researchLNK_backend.dtos.auth.AuthResponse;
import com.maahir.researchLNK_backend.dtos.auth.LoginRequest;
import com.maahir.researchLNK_backend.dtos.auth.RegistrationRequest;
import com.maahir.researchLNK_backend.dtos.auth.UserResponse;
import com.maahir.researchLNK_backend.persistence.model.User;
import com.maahir.researchLNK_backend.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegistrationRequest request) {
        AuthResponse authResponse = authService.register(
                request.getUserName(),
                request.getEmail(),
                request.getPassword(),
                request.getName());
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse authResponse = authService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(authResponse);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(@AuthenticationPrincipal User user) {
        UserResponse userResponse = authService.getCurrentUser(user);
        return ResponseEntity.ok(userResponse);
    }
}

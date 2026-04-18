package org.com.authservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.com.authservice.dto.JwtResponse;
import org.com.authservice.dto.LoginRequest;
import org.com.authservice.dto.RegisterRequest;
import org.com.authservice.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return  ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<JwtResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        return  ResponseEntity.ok(authService.register(registerRequest));
    }
}
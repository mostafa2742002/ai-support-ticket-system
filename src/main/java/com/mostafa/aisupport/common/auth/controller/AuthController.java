package com.mostafa.aisupport.common.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import com.mostafa.aisupport.common.auth.dto.LoginRequest;
import com.mostafa.aisupport.common.auth.dto.LoginResponse;
import com.mostafa.aisupport.common.auth.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication endpoints for internal support users")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(
            summary = "Login and get JWT token",
            description = "Authenticates an internal support user and returns a JWT token for protected endpoints."
    )
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
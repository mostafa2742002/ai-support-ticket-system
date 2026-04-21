package com.mostafa.aisupport.common.auth.controller;


import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import com.mostafa.aisupport.common.auth.dto.LoginRequest;
import com.mostafa.aisupport.common.auth.dto.LoginResponse;
import com.mostafa.aisupport.common.auth.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
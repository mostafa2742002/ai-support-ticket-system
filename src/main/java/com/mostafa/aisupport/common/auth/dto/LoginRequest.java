package com.mostafa.aisupport.common.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Login request for internal support users")
public record LoginRequest(

        @Schema(example = "agent")
        @NotBlank(message = "Username is required")
        String username,

        @Schema(example = "agent123")
        @NotBlank(message = "Password is required")
        String password
) {
}
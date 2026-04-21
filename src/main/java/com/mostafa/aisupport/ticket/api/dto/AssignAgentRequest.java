package com.mostafa.aisupport.ticket.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request body for assigning a ticket to a specific agent")
public record AssignAgentRequest(

        @Schema(example = "Sara")
        @NotBlank(message = "Assigned agent is required")
        @Size(max = 100, message = "Assigned agent must not exceed 100 characters")
        String assignedTo
) {
}
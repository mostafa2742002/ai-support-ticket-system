package com.mostafa.aisupport.ticket.api.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AssignAgentRequest(

        @NotBlank(message = "Assigned agent is required")
        @Size(max = 100, message = "Assigned agent must not exceed 100 characters")
        String assignedTo
) {
}
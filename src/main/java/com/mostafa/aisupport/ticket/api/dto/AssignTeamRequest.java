package com.mostafa.aisupport.ticket.api.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AssignTeamRequest(

        @NotBlank(message = "Assigned team is required")
        @Size(max = 50, message = "Assigned team must not exceed 50 characters")
        String assignedTeam
) {
}
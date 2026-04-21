package com.mostafa.aisupport.ticket.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request body for assigning a ticket to a support team")
public record AssignTeamRequest(

        @Schema(example = "BILLING")
        @NotBlank(message = "Assigned team is required")
        @Size(max = 50, message = "Assigned team must not exceed 50 characters")
        String assignedTeam
) {
}
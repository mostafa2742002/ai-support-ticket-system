package com.mostafa.aisupport.ticket.api.dto;



import com.mostafa.aisupport.ticket.domain.enums.TicketStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request body for updating ticket status")
public record UpdateTicketStatusRequest(

        @Schema(example = "IN_PROGRESS")
        @NotNull(message = "Status is required")
        TicketStatus status
) {
}
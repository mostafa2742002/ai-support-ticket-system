package com.mostafa.aisupport.ticket.api.dto;


import com.mostafa.aisupport.ticket.domain.enums.TicketStatus;

import jakarta.validation.constraints.NotNull;

public record UpdateTicketStatusRequest(

        @NotNull(message = "Status is required")
        TicketStatus status
) {
}
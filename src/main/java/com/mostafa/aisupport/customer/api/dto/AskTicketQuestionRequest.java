package com.mostafa.aisupport.customer.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AskTicketQuestionRequest(
        @NotNull(message = "Ticket id is required")
        Long ticketId,

        @NotBlank(message = "Customer email is required")
        @Email(message = "Customer email must be valid")
        String customerEmail,

        @NotBlank(message = "Question is required")
        String question
) {
}
package com.mostafa.aisupport.ticket.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request body for creating a support ticket")
public record CreateTicketRequest(

        @Schema(example = "Payment issue")
        @NotBlank(message = "Title is required")
        @Size(max = 150, message = "Title must not exceed 150 characters")
        String title,

        @Schema(example = "I was charged twice for my premium subscription and it still does not work.")
        @NotBlank(message = "Description is required")
        String description,

        @Schema(example = "Mostafa Mahmoud")
        @NotBlank(message = "Customer name is required")
        @Size(max = 100, message = "Customer name must not exceed 100 characters")
        String customerName,

        @Schema(example = "mostafa@email.com")
        @NotBlank(message = "Customer email is required")
        @Email(message = "Customer email must be valid")
        @Size(max = 150, message = "Customer email must not exceed 150 characters")
        String customerEmail
) {
}
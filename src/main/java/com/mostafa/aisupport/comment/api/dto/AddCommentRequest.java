package com.mostafa.aisupport.comment.api.dto;

import com.mostafa.aisupport.ticket.domain.enums.AuthorType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Request body for adding a ticket comment")
public record AddCommentRequest(

        @Schema(example = "AGENT")
        @NotNull(message = "Author type is required")
        AuthorType authorType,

        @Schema(example = "Sara")
        @NotBlank(message = "Author name is required")
        @Size(max = 100, message = "Author name must not exceed 100 characters")
        String authorName,

        @Schema(example = "We are currently investigating the billing issue.")
        @NotBlank(message = "Content is required")
        String content
) {
}
package com.mostafa.aisupport.comment.api.dto;


import com.mostafa.aisupport.ticket.domain.enums.AuthorType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AddCommentRequest(

        @NotNull(message = "Author type is required")
        AuthorType authorType,

        @NotBlank(message = "Author name is required")
        @Size(max = 100, message = "Author name must not exceed 100 characters")
        String authorName,

        @NotBlank(message = "Content is required")
        String content
) {
}
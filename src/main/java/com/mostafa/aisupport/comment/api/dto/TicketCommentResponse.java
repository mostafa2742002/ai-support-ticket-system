package com.mostafa.aisupport.comment.api.dto;


import java.time.LocalDateTime;

import com.mostafa.aisupport.comment.domain.entity.TicketComment;
import com.mostafa.aisupport.ticket.domain.enums.AuthorType;

public record TicketCommentResponse(
        Long id,
        Long ticketId,
        AuthorType authorType,
        String authorName,
        String content,
        LocalDateTime createdAt
) {
    public static TicketCommentResponse from(TicketComment comment) {
        return new TicketCommentResponse(
                comment.getId(),
                comment.getTicketId(),
                comment.getAuthorType(),
                comment.getAuthorName(),
                comment.getContent(),
                comment.getCreatedAt()
        );
    }
}
package com.mostafa.aisupport.comment.domain.entity;

import java.time.LocalDateTime;

import com.mostafa.aisupport.ticket.domain.enums.AuthorType;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class TicketComment {

    private Long id;
    private Long ticketId;
    private AuthorType authorType;
    private String authorName;
    private String content;
    private LocalDateTime createdAt;

    public static TicketComment create(
            Long ticketId,
            AuthorType authorType,
            String authorName,
            String content
    ) {
        return new TicketComment(
                null,
                ticketId,
                authorType,
                authorName,
                content,
                LocalDateTime.now()
        );
    }
}

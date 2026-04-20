package com.mostafa.aisupport.comment.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mostafa.aisupport.comment.domain.entity.TicketComment;
import com.mostafa.aisupport.comment.infrastructure.TicketCommentRepository;
import com.mostafa.aisupport.common.exception.NotFoundException;
import com.mostafa.aisupport.ticket.domain.enums.AuthorType;
import com.mostafa.aisupport.ticket.infrastructure.TicketRepository;

@Service
@Transactional
public class TicketCommentService {

    private final TicketCommentRepository ticketCommentRepository;
    private final TicketRepository ticketRepository;

    public TicketCommentService(
            TicketCommentRepository ticketCommentRepository,
            TicketRepository ticketRepository
    ) {
        this.ticketCommentRepository = ticketCommentRepository;
        this.ticketRepository = ticketRepository;
    }

    public TicketComment addComment(
            Long ticketId,
            AuthorType authorType,
            String authorName,
            String content
    ) {
        if (!ticketRepository.existsById(ticketId)) {
            throw new NotFoundException("Ticket not found with id: " + ticketId);
        }

        TicketComment comment = TicketComment.create(ticketId, authorType, authorName, content);
        return ticketCommentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public List<TicketComment> getCommentsByTicketId(Long ticketId) {
        if (!ticketRepository.existsById(ticketId)) {
            throw new NotFoundException("Ticket not found with id: " + ticketId);
        }

        return ticketCommentRepository.findByTicketIdOrderByCreatedAtAsc(ticketId);
    }
}
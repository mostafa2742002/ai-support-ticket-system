package com.mostafa.aisupport.comment.infrastructure;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mostafa.aisupport.comment.domain.entity.TicketComment;

public interface TicketCommentRepository extends JpaRepository<TicketComment, Long> {

    List<TicketComment> findByTicketIdOrderByCreatedAtAsc(Long ticketId);
}
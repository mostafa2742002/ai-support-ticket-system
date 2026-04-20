package com.mostafa.aisupport.comment.api.controller;


import com.mostafa.aisupport.comment.api.dto.AddCommentRequest;
import com.mostafa.aisupport.comment.api.dto.TicketCommentResponse;
import com.mostafa.aisupport.comment.application.TicketCommentService;
import com.mostafa.aisupport.comment.domain.entity.TicketComment;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets/{ticketId}/comments")
@Validated
public class TicketCommentController {

    private final TicketCommentService ticketCommentService;

    public TicketCommentController(TicketCommentService ticketCommentService) {
        this.ticketCommentService = ticketCommentService;
    }

    @PostMapping
    public TicketCommentResponse addComment(
            @PathVariable Long ticketId,
            @Valid @RequestBody AddCommentRequest request
    ) {
        TicketComment comment = ticketCommentService.addComment(
                ticketId,
                request.authorType(),
                request.authorName(),
                request.content()
        );

        return TicketCommentResponse.from(comment);
    }

    @GetMapping
    public List<TicketCommentResponse> getComments(@PathVariable Long ticketId) {
        return ticketCommentService.getCommentsByTicketId(ticketId)
                .stream()
                .map(TicketCommentResponse::from)
                .toList();
    }
}
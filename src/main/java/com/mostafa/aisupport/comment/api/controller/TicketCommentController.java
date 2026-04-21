package com.mostafa.aisupport.comment.api.controller;

import com.mostafa.aisupport.comment.api.dto.AddCommentRequest;
import com.mostafa.aisupport.comment.api.dto.TicketCommentResponse;
import com.mostafa.aisupport.comment.application.TicketCommentService;
import com.mostafa.aisupport.comment.domain.entity.TicketComment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(
        value = "/api/tickets/{ticketId}/comments",
        produces = MediaType.APPLICATION_JSON_VALUE
)
@Validated
@Tag(name = "Ticket Comments", description = "Comment and ticket history endpoints")
public class TicketCommentController {

    private final TicketCommentService ticketCommentService;

    public TicketCommentController(TicketCommentService ticketCommentService) {
        this.ticketCommentService = ticketCommentService;
    }

    @Operation(
            summary = "Add comment to ticket",
            description = "Protected endpoint. Adds a comment from customer, agent, or AI to a ticket."
    )
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
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

    @Operation(
            summary = "Get ticket comments",
            description = "Protected endpoint. Returns ticket comments in chronological order."
    )
    @GetMapping
    public List<TicketCommentResponse> getComments(@PathVariable Long ticketId) {
        return ticketCommentService.getCommentsByTicketId(ticketId)
                .stream()
                .map(TicketCommentResponse::from)
                .toList();
    }
}
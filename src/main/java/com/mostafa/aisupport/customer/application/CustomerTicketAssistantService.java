package com.mostafa.aisupport.customer.application;

import com.mostafa.aisupport.comment.application.TicketCommentService;
import com.mostafa.aisupport.comment.domain.entity.TicketComment;
import com.mostafa.aisupport.common.exception.BadRequestException;
import com.mostafa.aisupport.ticket.application.TicketService;
import com.mostafa.aisupport.ticket.domain.entity.Ticket;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CustomerTicketAssistantService {

    private final TicketService ticketService;
    private final TicketCommentService ticketCommentService;
    private final ChatClient chatClient;

    public CustomerTicketAssistantService(
            TicketService ticketService,
            TicketCommentService ticketCommentService,
            ChatClient chatClient
    ) {
        this.ticketService = ticketService;
        this.ticketCommentService = ticketCommentService;
        this.chatClient = chatClient;
    }

    public String answerQuestion(Long ticketId, String customerEmail, String question) {
        Ticket ticket = ticketService.getTicketById(ticketId);

        if (!ticket.getCustomerEmail().equalsIgnoreCase(customerEmail)) {
            throw new BadRequestException("Ticket id and customer email do not match");
        }

        List<TicketComment> comments = ticketCommentService.getCommentsByTicketId(ticketId);

        String commentsText = comments.stream()
                .map(comment -> "- [" + comment.getAuthorType() + "] "
                        + comment.getAuthorName() + ": " + comment.getContent())
                .reduce("", (a, b) -> a + b + "\n");

        String prompt = """
                You are a customer support assistant.

                Answer the customer question only using the ticket data below.
                Do not invent any information.
                Be clear, short, and customer-friendly.
                If the requested information is not available, say that clearly.

                Ticket details:
                - Ticket ID: %d
                - Title: %s
                - Description: %s
                - Status: %s
                - Triage Status: %s
                - Category: %s
                - Priority: %s
                - Assigned Team: %s
                - Assigned To: %s
                - AI Summary: %s

                Ticket comments:
                %s

                Customer question:
                %s
                """.formatted(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getStatus(),
                ticket.getTriageStatus(),
                ticket.getCategory(),
                ticket.getPriority(),
                ticket.getAssignedTeam(),
                ticket.getAssignedTo(),
                ticket.getAiSummary(),
                commentsText.isBlank() ? "No comments yet." : commentsText,
                question
        );

        return chatClient.prompt()
                .system("You are a safe support assistant that answers only from provided ticket data.")
                .user(prompt)
                .call()
                .content();
    }
}
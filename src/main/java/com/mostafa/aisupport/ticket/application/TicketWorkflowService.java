package com.mostafa.aisupport.ticket.application;



import com.mostafa.aisupport.ai.application.TicketTriageAgentService;
import com.mostafa.aisupport.comment.application.TicketCommentService;
import com.mostafa.aisupport.ticket.domain.entity.Ticket;
import com.mostafa.aisupport.ticket.domain.enums.AuthorType;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TicketWorkflowService {

    private final TicketService ticketService;
    private final TicketTriageAgentService ticketTriageAgentService;
    private final TicketCommentService ticketCommentService;

    public TicketWorkflowService(
            TicketService ticketService,
            TicketTriageAgentService ticketTriageAgentService,
            TicketCommentService ticketCommentService
    ) {
        this.ticketService = ticketService;
        this.ticketTriageAgentService = ticketTriageAgentService;
        this.ticketCommentService = ticketCommentService;
    }

    public Ticket createTicketAndRunTriage(
            String title,
            String description,
            String customerName,
            String customerEmail
    ) {
        Ticket createdTicket = ticketService.createTicket(
                title,
                description,
                customerName,
                customerEmail
        );

        try {
            ticketTriageAgentService.triageTicket(createdTicket.getId());
        } catch (Exception ex) {
            ticketCommentService.addComment(
                    createdTicket.getId(),
                    AuthorType.AI,
                    "AI Triage Agent",
                    "Automatic triage failed: " + ex.getMessage()
            );
        }

        return ticketService.getTicketById(createdTicket.getId());
    }
}
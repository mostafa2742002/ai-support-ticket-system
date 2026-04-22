package com.mostafa.aisupport.ticket.application;

import com.mostafa.aisupport.agent.application.AgentAssignmentService;
import com.mostafa.aisupport.ai.application.TicketTriageAgentService;
import com.mostafa.aisupport.comment.application.TicketCommentService;
import com.mostafa.aisupport.common.metrics.TicketMetricsService;
import com.mostafa.aisupport.ticket.domain.entity.Ticket;
import com.mostafa.aisupport.ticket.domain.enums.AuthorType;
import com.mostafa.aisupport.ticket.event.TicketEventPublisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
@Service
public class TicketWorkflowService {

    private static final Logger log =
            LoggerFactory.getLogger(TicketWorkflowService.class);

    private final TicketService ticketService;
    private final TicketEventPublisher ticketEventPublisher;
    private final TicketMetricsService ticketMetricsService;

    public TicketWorkflowService(
            TicketService ticketService,
            TicketEventPublisher ticketEventPublisher,
            TicketMetricsService ticketMetricsService
    ) {
        this.ticketService = ticketService;
        this.ticketEventPublisher = ticketEventPublisher;
        this.ticketMetricsService = ticketMetricsService;
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

        ticketMetricsService.incrementTicketCreated();
        ticketEventPublisher.publishTicketCreated(createdTicket.getId());

        log.info("Ticket created and async triage event published for ticketId={}", createdTicket.getId());

        return createdTicket;
    }
}
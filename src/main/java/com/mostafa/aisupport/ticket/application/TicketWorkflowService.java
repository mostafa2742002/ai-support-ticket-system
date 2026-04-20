package com.mostafa.aisupport.ticket.application;



import com.mostafa.aisupport.ai.application.TicketTriageAgentService;
import com.mostafa.aisupport.ticket.domain.entity.Ticket;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TicketWorkflowService {

    private final TicketService ticketService;
    private final TicketTriageAgentService ticketTriageAgentService;

    public TicketWorkflowService(
            TicketService ticketService,
            TicketTriageAgentService ticketTriageAgentService
    ) {
        this.ticketService = ticketService;
        this.ticketTriageAgentService = ticketTriageAgentService;
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

        ticketTriageAgentService.triageTicket(createdTicket.getId());

        return ticketService.getTicketById(createdTicket.getId());
    }
}
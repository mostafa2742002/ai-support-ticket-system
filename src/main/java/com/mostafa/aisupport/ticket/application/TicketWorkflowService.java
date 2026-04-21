package com.mostafa.aisupport.ticket.application;

import com.mostafa.aisupport.agent.application.AgentAssignmentService;
import com.mostafa.aisupport.ai.application.TicketTriageAgentService;
import com.mostafa.aisupport.comment.application.TicketCommentService;
import com.mostafa.aisupport.ticket.domain.entity.Ticket;
import com.mostafa.aisupport.ticket.domain.enums.AuthorType;

import org.springframework.stereotype.Service;

@Service
public class TicketWorkflowService {

    private final TicketService ticketService;
    private final TicketTriageAgentService ticketTriageAgentService;
    private final TicketCommentService ticketCommentService;
    private final AgentAssignmentService agentAssignmentService;

    public TicketWorkflowService(
            TicketService ticketService,
            TicketTriageAgentService ticketTriageAgentService,
            TicketCommentService ticketCommentService,
            AgentAssignmentService agentAssignmentService
    ) {
        this.ticketService = ticketService;
        this.ticketTriageAgentService = ticketTriageAgentService;
        this.ticketCommentService = ticketCommentService;
        this.agentAssignmentService = agentAssignmentService;
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

            Ticket updatedTicket = ticketService.getTicketById(createdTicket.getId());

            if (updatedTicket.getAssignedTeam() != null
                    && updatedTicket.getAssignedTo() == null) {

                String agentName = agentAssignmentService.findBestAgentName(
                        updatedTicket.getAssignedTeam()
                );

                if (agentName != null) {
                    ticketService.assignTicketToAgent(
                            updatedTicket.getId(),
                            agentName
                    );

                    ticketCommentService.addComment(
                            updatedTicket.getId(),
                            AuthorType.AI,
                            "Routing Engine",
                            "Automatically assigned to agent: " + agentName
                    );
                }
            }

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
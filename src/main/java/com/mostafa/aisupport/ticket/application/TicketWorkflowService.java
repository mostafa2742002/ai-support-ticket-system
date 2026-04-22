package com.mostafa.aisupport.ticket.application;

import com.mostafa.aisupport.agent.application.AgentAssignmentService;
import com.mostafa.aisupport.ai.application.TicketTriageAgentService;
import com.mostafa.aisupport.comment.application.TicketCommentService;
import com.mostafa.aisupport.common.metrics.TicketMetricsService;
import com.mostafa.aisupport.ticket.domain.entity.Ticket;
import com.mostafa.aisupport.ticket.domain.enums.AuthorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TicketWorkflowService {

    private final TicketService ticketService;
    private final TicketTriageAgentService ticketTriageAgentService;
    private final TicketCommentService ticketCommentService;
    private final AgentAssignmentService agentAssignmentService;
    private final TicketMetricsService ticketMetricsService;
    private static final Logger log = LoggerFactory.getLogger(TicketWorkflowService.class);

    public TicketWorkflowService(
            TicketService ticketService,
            TicketTriageAgentService ticketTriageAgentService,
            TicketCommentService ticketCommentService,
            AgentAssignmentService agentAssignmentService,
            TicketMetricsService ticketMetricsService) {
        this.ticketService = ticketService;
        this.ticketTriageAgentService = ticketTriageAgentService;
        this.ticketCommentService = ticketCommentService;
        this.agentAssignmentService = agentAssignmentService;
        this.ticketMetricsService = ticketMetricsService;
    }

    public Ticket createTicketAndRunTriage(
            String title,
            String description,
            String customerName,
            String customerEmail) {
        Ticket createdTicket = ticketService.createTicket(
                title,
                description,
                customerName,
                customerEmail);
        ticketMetricsService.incrementTicketCreated();
        log.info("Ticket created with id={}", createdTicket.getId());

        try {
            ticketTriageAgentService.triageTicket(createdTicket.getId());
            log.info("AI triage completed for ticketId={}", createdTicket.getId());
            ticketMetricsService.incrementTriageSuccess();

            Ticket updatedTicket = ticketService.getTicketById(createdTicket.getId());

            if (updatedTicket.getAssignedTeam() != null
                    && updatedTicket.getAssignedTo() == null) {

                String agentName = agentAssignmentService.findBestAgentName(
                        updatedTicket.getAssignedTeam());

                if (agentName != null) {
                    ticketService.assignTicketToAgent(
                            updatedTicket.getId(),
                            agentName);
                    
                    log.info("Ticket {} assigned to agent {}", updatedTicket.getId(), agentName);

                    ticketCommentService.addComment(
                            updatedTicket.getId(),
                            AuthorType.AI,
                            "Routing Engine",
                            "Automatically assigned to agent: " + agentName);
                }
            }

        } catch (Exception ex) {
            log.error("Automatic triage failed for ticketId={}", createdTicket.getId(), ex);
            ticketMetricsService.incrementTriageFailure();
            ticketCommentService.addComment(
                    createdTicket.getId(),
                    AuthorType.AI,
                    "AI Triage Agent",
                    "Automatic triage failed: " + ex.getMessage());
        }

        

        return ticketService.getTicketById(createdTicket.getId());
    }

}
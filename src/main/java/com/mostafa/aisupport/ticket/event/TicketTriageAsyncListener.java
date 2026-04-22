package com.mostafa.aisupport.ticket.event;


import com.mostafa.aisupport.agent.application.AgentAssignmentService;
import com.mostafa.aisupport.ai.application.TicketTriageAgentService;
import com.mostafa.aisupport.comment.application.TicketCommentService;
import com.mostafa.aisupport.common.metrics.TicketMetricsService;
import com.mostafa.aisupport.ticket.application.TicketService;
import com.mostafa.aisupport.ticket.domain.entity.Ticket;
import com.mostafa.aisupport.ticket.domain.enums.AuthorType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class TicketTriageAsyncListener {

    private static final Logger log =
            LoggerFactory.getLogger(TicketTriageAsyncListener.class);

    private final TicketService ticketService;
    private final TicketTriageAgentService ticketTriageAgentService;
    private final TicketCommentService ticketCommentService;
    private final AgentAssignmentService agentAssignmentService;
    private final TicketMetricsService ticketMetricsService;

    public TicketTriageAsyncListener(
            TicketService ticketService,
            TicketTriageAgentService ticketTriageAgentService,
            TicketCommentService ticketCommentService,
            AgentAssignmentService agentAssignmentService,
            TicketMetricsService ticketMetricsService
    ) {
        this.ticketService = ticketService;
        this.ticketTriageAgentService = ticketTriageAgentService;
        this.ticketCommentService = ticketCommentService;
        this.agentAssignmentService = agentAssignmentService;
        this.ticketMetricsService = ticketMetricsService;
    }

    @Async
    @EventListener
    public void handleTicketCreated(TicketCreatedEvent event) {
        Long ticketId = event.ticketId();

        try {
            log.info("Async triage started for ticketId={}", ticketId);

            ticketTriageAgentService.triageTicket(ticketId);

            Ticket updatedTicket = ticketService.getTicketById(ticketId);

            if (updatedTicket.getAssignedTeam() != null
                    && updatedTicket.getAssignedTo() == null) {

                String agentName = agentAssignmentService.findBestAgentName(
                        updatedTicket.getAssignedTeam()
                );

                if (agentName != null) {
                    ticketService.assignTicketToAgent(ticketId, agentName);

                    ticketCommentService.addComment(
                            ticketId,
                            AuthorType.AI,
                            "Routing Engine",
                            "Automatically assigned to agent: " + agentName
                    );
                }
            }

            ticketService.markTriageCompleted(ticketId);
            ticketMetricsService.incrementTriageSuccess();

            log.info("Async triage completed for ticketId={}", ticketId);

        } catch (Exception ex) {
            ticketService.markTriageFailed(ticketId);
            ticketMetricsService.incrementTriageFailure();

            ticketCommentService.addComment(
                    ticketId,
                    AuthorType.AI,
                    "AI Triage Agent",
                    "Async triage failed: " + ex.getMessage()
            );

            log.error("Async triage failed for ticketId={}", ticketId, ex);
        }
    }
}
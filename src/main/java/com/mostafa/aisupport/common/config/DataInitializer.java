package com.mostafa.aisupport.common.config;


import com.mostafa.aisupport.agent.domain.entity.Agent;
import com.mostafa.aisupport.agent.infrastructure.AgentRepository;
import com.mostafa.aisupport.comment.domain.entity.TicketComment;
import com.mostafa.aisupport.comment.infrastructure.TicketCommentRepository;
import com.mostafa.aisupport.ticket.domain.entity.Ticket;
import com.mostafa.aisupport.ticket.domain.enums.AgentTeam;
import com.mostafa.aisupport.ticket.domain.enums.AuthorType;
import com.mostafa.aisupport.ticket.domain.enums.TicketCategory;
import com.mostafa.aisupport.ticket.domain.enums.TicketPriority;
import com.mostafa.aisupport.ticket.infrastructure.TicketRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedData(
            AgentRepository agentRepository,
            TicketRepository ticketRepository,
            TicketCommentRepository ticketCommentRepository
    ) {
        return args -> {
            if (agentRepository.count() == 0) {
                Agent billingAgent = Agent.createActiveAgent(
                        "Sara",
                        "sara@support.com",
                        AgentTeam.BILLING
                );

                Agent technicalAgent = Agent.createActiveAgent(
                        "Ahmed",
                        "ahmed@support.com",
                        AgentTeam.TECHNICAL_SUPPORT
                );

                agentRepository.save(billingAgent);
                agentRepository.save(technicalAgent);
            }

            if (ticketRepository.count() == 0) {
                Ticket ticket = Ticket.createNew(
                        "Payment issue",
                        "I was charged twice for my subscription and no one replied to me.",
                        "Mostafa Mahmoud",
                        "mostafa@email.com"
                );

                ticket.updateCategory(TicketCategory.BILLING);
                ticket.updatePriority(TicketPriority.HIGH);
                ticket.assignTeam("BILLING");
                ticket.updateAiSummary("Customer reports a duplicate subscription charge and delayed support response.");

                Ticket savedTicket = ticketRepository.save(ticket);

                TicketComment aiComment = TicketComment.create(
                        savedTicket.getId(),
                        AuthorType.AI,
                        "AI Triage Agent",
                        "Classified as BILLING with HIGH priority and assigned to BILLING team."
                );

                ticketCommentRepository.save(aiComment);
            }
        };
    }
}
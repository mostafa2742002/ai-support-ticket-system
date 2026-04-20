package com.mostafa.aisupport.ai.application;



import com.mostafa.aisupport.comment.application.TicketCommentService;
import com.mostafa.aisupport.ticket.application.TicketService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.mostafa.aisupport.ai.api.dto.AiTriageResult;
import com.mostafa.aisupport.ticket.domain.entity.Ticket;
import com.mostafa.aisupport.ticket.domain.enums.AuthorType;

@Service
@Transactional
public class TicketTriageAgentService {

    private final ChatClient chatClient;
    private final TicketService ticketService;
    private final TicketCommentService ticketCommentService;

    public TicketTriageAgentService(
            ChatClient chatClient,
            TicketService ticketService,
            TicketCommentService ticketCommentService
    ) {
        this.chatClient = chatClient;
        this.ticketService = ticketService;
        this.ticketCommentService = ticketCommentService;
    }

    public AiTriageResult triageTicket(Long ticketId) {
        Ticket ticket = ticketService.getTicketById(ticketId);

        String prompt = buildPrompt(ticket);

        AiTriageResult result = chatClient.prompt()
                .user(prompt)
                .call()
                .entity(AiTriageResult.class);

        ticketService.applyAiTriageResult(
                ticketId,
                result.assignedTeam(),
                result.aiSummary(),
                result.category(),
                result.priority()
        );

        ticketCommentService.addComment(
                ticketId,
                AuthorType.AI,
                "AI Triage Agent",
                "Classified as " + result.category()
                        + " with priority " + result.priority()
                        + ", assigned team " + result.assignedTeam()
                        + ". Summary: " + result.aiSummary()
        );

        return result;
    }

    private String buildPrompt(Ticket ticket) {
        return """
                You are an AI support triage agent.

                Your job is to analyze the support ticket and return a structured result.

                Allowed ticket categories:
                - BILLING
                - TECHNICAL
                - ACCOUNT
                - GENERAL
                - FEATURE_REQUEST

                Allowed ticket priorities:
                - LOW
                - MEDIUM
                - HIGH
                - URGENT

                Allowed assignedTeam values:
                - BILLING
                - TECHNICAL_SUPPORT
                - CUSTOMER_SUCCESS

                Rules:
                - BILLING issues go to BILLING
                - TECHNICAL issues go to TECHNICAL_SUPPORT
                - ACCOUNT, GENERAL, and FEATURE_REQUEST usually go to CUSTOMER_SUCCESS
                - aiSummary must be short and professional
                - Return only valid enum values for category and priority
                - Return only one of the allowed assignedTeam values

                Return the result in a format that can be mapped directly to this Java structure:
                {
                "category": "...",
                "priority": "...",
                "assignedTeam": "...",
                "aiSummary": "..."
                }

                Ticket title:
                %s

                Ticket description:
                %s

                Customer name:
                %s

                Customer email:
                %s
                """.formatted(
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getCustomerName(),
                ticket.getCustomerEmail()
        );
    }
}
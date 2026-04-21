package com.mostafa.aisupport.ai.application;



import com.mostafa.aisupport.comment.application.TicketCommentService;
import com.mostafa.aisupport.ticket.application.TicketService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.mostafa.aisupport.ai.api.dto.AiTriageResult;
import com.mostafa.aisupport.ai.tools.service.TicketSupportTools;
import com.mostafa.aisupport.ticket.domain.entity.Ticket;
import com.mostafa.aisupport.ticket.domain.enums.AuthorType;
@Service
@Transactional
public class TicketTriageAgentService {

    private final ChatClient chatClient;
    private final TicketService ticketService;
    private final TicketCommentService ticketCommentService;
    private final TicketSupportTools ticketSupportTools;

    public TicketTriageAgentService(
            ChatClient chatClient,
            TicketService ticketService,
            TicketCommentService ticketCommentService,
            TicketSupportTools ticketSupportTools
    ) {
        this.chatClient = chatClient;
        this.ticketService = ticketService;
        this.ticketCommentService = ticketCommentService;
        this.ticketSupportTools = ticketSupportTools;
    }

    public AiTriageResult triageTicket(Long ticketId) {
        Ticket ticket = ticketService.getTicketById(ticketId);

        String prompt = buildPrompt(ticket);

        AiTriageResult result = chatClient.prompt()
                .system("""
                        You are an AI support triage agent.

                        You may use tools when helpful.
                        Use tools especially when:
                        - you want to inspect existing comments
                        - you want to review similar tickets
                        - you want to inspect active agents for a selected team

                        Be accurate and use only allowed enum values.
                        """)
                .user(prompt)
                .tools(ticketSupportTools)
                .call()
                .entity(AiTriageResult.class);
        
        System.out.println("AI triage result = " + result);

        validateTriageResult(result);

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
                Analyze the support ticket and return a structured result.

                You should inspect the current ticket comments first.
                If classification is unclear, inspect recent similar tickets by category.
                If you choose a team, you may inspect active agents in that team.

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

                Return the result in this JSON shape:
                {
                "category": "BILLING",
                "priority": "HIGH",
                "assignedTeam": "BILLING",
                "aiSummary": "Short summary"
                }

                Current ticket id:
                %d

                Ticket title:
                %s

                Ticket description:
                %s

                Customer name:
                %s

                Customer email:
                %s
                """.formatted(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getCustomerName(),
                ticket.getCustomerEmail()
        );
    }


        private void validateTriageResult(AiTriageResult result) {
                if (result == null) {
                        throw new IllegalStateException("AI triage result is null");
                }

                if (result.category() == null) {
                        throw new IllegalStateException("AI triage result missing category");
                }

                if (result.priority() == null) {
                        throw new IllegalStateException("AI triage result missing priority");
                }

                if (result.assignedTeam() == null || result.assignedTeam().isBlank()) {
                        throw new IllegalStateException("AI triage result missing assignedTeam");
                }

                if (result.aiSummary() == null || result.aiSummary().isBlank()) {
                        throw new IllegalStateException("AI triage result missing aiSummary");
                }
        }

}
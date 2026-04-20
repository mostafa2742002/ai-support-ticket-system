package com.mostafa.aisupport.ai.tools.service;



import com.mostafa.aisupport.agent.infrastructure.AgentRepository;
import com.mostafa.aisupport.ai.tools.dto.AgentView;
import com.mostafa.aisupport.ai.tools.dto.CommentView;
import com.mostafa.aisupport.ai.tools.dto.SimilarTicketView;
import com.mostafa.aisupport.comment.infrastructure.TicketCommentRepository;
import com.mostafa.aisupport.ticket.domain.enums.AgentTeam;
import com.mostafa.aisupport.ticket.domain.enums.TicketCategory;
import com.mostafa.aisupport.ticket.infrastructure.TicketRepository;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TicketSupportTools {

    private final TicketRepository ticketRepository;
    private final AgentRepository agentRepository;
    private final TicketCommentRepository ticketCommentRepository;

    public TicketSupportTools(
            TicketRepository ticketRepository,
            AgentRepository agentRepository,
            TicketCommentRepository ticketCommentRepository
    ) {
        this.ticketRepository = ticketRepository;
        this.agentRepository = agentRepository;
        this.ticketCommentRepository = ticketCommentRepository;
    }

    @Tool(description = "Find up to 5 recent tickets by category to help compare similar support cases")
    public List<SimilarTicketView> findRecentTicketsByCategory(TicketCategory category) {
        return ticketRepository.findTop5ByCategoryOrderByCreatedAtDesc(category)
                .stream()
                .map(ticket -> new SimilarTicketView(
                        ticket.getId(),
                        ticket.getTitle(),
                        ticket.getCategory() != null ? ticket.getCategory().name() : null,
                        ticket.getPriority() != null ? ticket.getPriority().name() : null,
                        ticket.getAssignedTeam()
                ))
                .toList();
    }

    @Tool(description = "Get active support agents for a given team")
    public List<AgentView> getActiveAgentsByTeam(AgentTeam team) {
        return agentRepository.findByTeamAndActiveTrue(team)
                .stream()
                .map(agent -> new AgentView(
                        agent.getId(),
                        agent.getName(),
                        agent.getEmail(),
                        agent.getTeam().name()
                ))
                .toList();
    }

    @Tool(description = "Get all comments for a ticket in chronological order")
    public List<CommentView> getTicketComments(Long ticketId) {
        return ticketCommentRepository.findByTicketIdOrderByCreatedAtAsc(ticketId)
                .stream()
                .map(comment -> new CommentView(
                        comment.getAuthorType().name(),
                        comment.getAuthorName(),
                        comment.getContent()
                ))
                .toList();
    }
}
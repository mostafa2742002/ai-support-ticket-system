package com.mostafa.aisupport.agent.application;

import com.mostafa.aisupport.agent.domain.entity.Agent;
import com.mostafa.aisupport.agent.infrastructure.AgentRepository;
import com.mostafa.aisupport.ticket.domain.enums.AgentTeam;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class AgentAssignmentService {

    private final AgentRepository agentRepository;

    public AgentAssignmentService(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
    }

    public String findBestAgentName(String assignedTeam) {
        AgentTeam team = AgentTeam.valueOf(assignedTeam);

        List<Agent> activeAgents = agentRepository.findByTeamAndActiveTrue(team);

        if (activeAgents.isEmpty()) {
            return null;
        }

        return activeAgents.get(0).getName();
    }
}
package com.mostafa.aisupport.agent.application;

import com.mostafa.aisupport.agent.domain.entity.Agent;
import com.mostafa.aisupport.agent.infrastructure.AgentRepository;
import com.mostafa.aisupport.ticket.domain.enums.AgentTeam;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AgentAssignmentServiceTest {

    private AgentRepository agentRepository;
    private AgentAssignmentService agentAssignmentService;

    @BeforeEach
    void setUp() {
        agentRepository = mock(AgentRepository.class);
        agentAssignmentService = new AgentAssignmentService(agentRepository);
    }

    @Test
    void shouldReturnFirstActiveAgentNameForTeam() {
        Agent sara = Agent.createActiveAgent("Sara", "sara@support.com", AgentTeam.BILLING);
        Agent nada = Agent.createActiveAgent("Nada", "nada@support.com", AgentTeam.BILLING);

        when(agentRepository.findByTeamAndActiveTrue(AgentTeam.BILLING))
                .thenReturn(List.of(sara, nada));

        String result = agentAssignmentService.findBestAgentName("BILLING");

        assertThat(result).isEqualTo("Sara");
    }

    @Test
    void shouldReturnNullWhenNoActiveAgentExists() {
        when(agentRepository.findByTeamAndActiveTrue(AgentTeam.TECHNICAL_SUPPORT))
                .thenReturn(List.of());

        String result = agentAssignmentService.findBestAgentName("TECHNICAL_SUPPORT");

        assertThat(result).isNull();
    }
}
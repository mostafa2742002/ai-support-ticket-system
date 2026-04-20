package com.mostafa.aisupport.agent.infrastructure;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mostafa.aisupport.agent.domain.entity.Agent;
import com.mostafa.aisupport.ticket.domain.enums.AgentTeam;

public interface AgentRepository extends JpaRepository<Agent, Long> {

    Optional<Agent> findByEmail(String email);

    List<Agent> findByTeamAndActiveTrue(AgentTeam team);
}
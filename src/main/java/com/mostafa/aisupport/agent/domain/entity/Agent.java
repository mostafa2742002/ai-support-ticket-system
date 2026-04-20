package com.mostafa.aisupport.agent.domain.entity;

import com.mostafa.aisupport.ticket.domain.enums.AgentTeam;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Agent {

    private Long id;
    private String name;
    private String email;
    private AgentTeam team;
    private boolean active;

    public static Agent createActiveAgent(String name, String email, AgentTeam team) {
        return new Agent(null, name, email, team, true);
    }
}

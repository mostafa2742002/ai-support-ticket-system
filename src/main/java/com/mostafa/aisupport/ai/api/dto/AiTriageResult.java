package com.mostafa.aisupport.ai.api.dto;

import com.mostafa.aisupport.ticket.domain.enums.TicketCategory;
import com.mostafa.aisupport.ticket.domain.enums.TicketPriority;

public record AiTriageResult(
        TicketCategory category,
        TicketPriority priority,
        String assignedTeam,
        String aiSummary
) {
}
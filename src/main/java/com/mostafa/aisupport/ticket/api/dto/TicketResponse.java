package com.mostafa.aisupport.ticket.api.dto;

import java.time.LocalDateTime;

import com.mostafa.aisupport.ticket.domain.entity.Ticket;
import com.mostafa.aisupport.ticket.domain.enums.TicketCategory;
import com.mostafa.aisupport.ticket.domain.enums.TicketPriority;
import com.mostafa.aisupport.ticket.domain.enums.TicketStatus;
import com.mostafa.aisupport.ticket.domain.enums.TriageStatus;

public record TicketResponse(
        Long id,
        String title,
        String description,
        String customerName,
        String customerEmail,
        TicketStatus status,
        TicketCategory category,
        TicketPriority priority,
        TriageStatus triageStatus,
        String assignedTeam,
        String assignedTo,
        String aiSummary,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static TicketResponse from(Ticket ticket) {
        return new TicketResponse(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getCustomerName(),
                ticket.getCustomerEmail(),
                ticket.getStatus(),
                ticket.getCategory(),
                ticket.getPriority(),
                ticket.getTriageStatus(),
                ticket.getAssignedTeam(),
                ticket.getAssignedTo(),
                ticket.getAiSummary(),
                ticket.getCreatedAt(),
                ticket.getUpdatedAt()
        );
    }
}
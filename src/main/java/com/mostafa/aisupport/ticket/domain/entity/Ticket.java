package com.mostafa.aisupport.ticket.domain.entity;

import java.time.LocalDateTime;

import com.mostafa.aisupport.ticket.domain.enums.TicketCategory;
import com.mostafa.aisupport.ticket.domain.enums.TicketPriority;
import com.mostafa.aisupport.ticket.domain.enums.TicketStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Ticket {

    private Long id;
    private String title;
    private String description;
    private String customerName;
    private String customerEmail;
    private TicketStatus status;
    private TicketCategory category;
    private TicketPriority priority;
    private String assignedTeam;
    private String assignedTo;
    private String aiSummary;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Ticket createNew(
                        String title,
                        String description,
                        String customerName,
                        String customerEmail
                        ) {
        LocalDateTime now = LocalDateTime.now();

        return new Ticket(
                null,
                title,
                description,
                customerName,
                customerEmail,
                TicketStatus.OPEN,
                null,
                TicketPriority.MEDIUM,
                null,
                null,
                null,
                now,
                now
        );
    }

    public void assignTeam(String assignedTeam) {
        this.assignedTeam = assignedTeam;
        this.updatedAt = LocalDateTime.now();
    }

    public void assignTo(String assignedTo) {
        this.assignedTo = assignedTo;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateStatus(TicketStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateCategory(TicketCategory category) {
        this.category = category;
        this.updatedAt = LocalDateTime.now();
    }

    public void updatePriority(TicketPriority priority) {
        this.priority = priority;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateAiSummary(String aiSummary) {
        this.aiSummary = aiSummary;
        this.updatedAt = LocalDateTime.now();
    }
}

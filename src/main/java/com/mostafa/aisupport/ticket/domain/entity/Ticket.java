package com.mostafa.aisupport.ticket.domain.entity;

import java.time.LocalDateTime;

import com.mostafa.aisupport.ticket.domain.enums.TicketCategory;
import com.mostafa.aisupport.ticket.domain.enums.TicketPriority;
import com.mostafa.aisupport.ticket.domain.enums.TicketStatus;
import com.mostafa.aisupport.ticket.domain.enums.TriageStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "customer_name", nullable = false, length = 100)
    private String customerName;

    @Column(name = "customer_email", nullable = false, length = 150)
    private String customerEmail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TicketStatus status;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private TicketCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TicketPriority priority;

    @Enumerated(EnumType.STRING)
    @Column(name = "triage_status", nullable = false, length = 20)
    private TriageStatus triageStatus;

    @Column(name = "assigned_team", length = 50)
    private String assignedTeam;

    @Column(name = "assigned_to", length = 100)
    private String assignedTo;

    @Column(name = "ai_summary", columnDefinition = "TEXT")
    private String aiSummary;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
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
                TriageStatus.PENDING,
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

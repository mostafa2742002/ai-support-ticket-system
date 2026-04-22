package com.mostafa.aisupport.ticket.api.controller;

import com.mostafa.aisupport.ticket.api.dto.AssignAgentRequest;
import com.mostafa.aisupport.ticket.api.dto.AssignTeamRequest;
import com.mostafa.aisupport.ticket.api.dto.CreateTicketRequest;
import com.mostafa.aisupport.ticket.api.dto.TicketResponse;
import com.mostafa.aisupport.ticket.api.dto.UpdateTicketStatusRequest;
import com.mostafa.aisupport.ticket.application.TicketService;
import com.mostafa.aisupport.ticket.application.TicketWorkflowService;
import com.mostafa.aisupport.ticket.domain.entity.Ticket;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/tickets", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
@Tag(name = "Tickets", description = "Ticket management endpoints")
public class TicketController {

    private final TicketService ticketService;
    private final TicketWorkflowService ticketWorkflowService;

    public TicketController(
            TicketService ticketService,
            TicketWorkflowService ticketWorkflowService
    ) {
        this.ticketService = ticketService;
        this.ticketWorkflowService = ticketWorkflowService;
    }

    @Operation(
            summary = "Create a new support ticket",
            description = "Public endpoint. Creates a ticket and automatically runs AI triage and routing."
    )
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public TicketResponse createTicket(@Valid @RequestBody CreateTicketRequest request) {
        Ticket ticket = ticketWorkflowService.createTicketAndRunTriage(
                request.title(),
                request.description(),
                request.customerName(),
                request.customerEmail()
        );

        return TicketResponse.from(ticket);
    }

    @Operation(
            summary = "Get ticket by ID",
            description = "Protected endpoint. Returns the full details of a ticket."
    )
    @PreAuthorize("hasAnyRole('AGENT', 'ADMIN')")
    @GetMapping("/{id}")
    public TicketResponse getTicketById(@PathVariable Long id) {
        return TicketResponse.from(ticketService.getTicketById(id));
    }

    @Operation(
            summary = "Get all tickets",
            description = "Protected endpoint. Returns all tickets in the system."
    )
    @PreAuthorize("hasAnyRole('AGENT', 'ADMIN')")
    @GetMapping
    public List<TicketResponse> getAllTickets() {
        return ticketService.getAllTickets()
                .stream()
                .map(TicketResponse::from)
                .toList();
    }

    @Operation(
            summary = "Assign ticket to team",
            description = "Protected endpoint. Updates the assigned support team for a ticket."
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(value = "/{id}/assign-team", consumes = MediaType.APPLICATION_JSON_VALUE)
    public TicketResponse assignTeam(
            @PathVariable Long id,
            @Valid @RequestBody AssignTeamRequest request
    ) {
        return TicketResponse.from(
                ticketService.assignTicketToTeam(id, request.assignedTeam())
        );
    }

    @Operation(
            summary = "Assign ticket to agent",
            description = "Protected endpoint. Updates the assigned agent for a ticket."
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(value = "/{id}/assign-agent", consumes = MediaType.APPLICATION_JSON_VALUE)
    public TicketResponse assignAgent(
            @PathVariable Long id,
            @Valid @RequestBody AssignAgentRequest request
    ) {
        return TicketResponse.from(
                ticketService.assignTicketToAgent(id, request.assignedTo())
        );
    }

    @Operation(
            summary = "Update ticket status",
            description = "Protected endpoint. Changes the workflow status of a ticket."
    )
    @PreAuthorize("hasAnyRole('AGENT', 'ADMIN')")
    @PatchMapping(value = "/{id}/status", consumes = MediaType.APPLICATION_JSON_VALUE) 
    public TicketResponse updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTicketStatusRequest request
    ) {
        return TicketResponse.from(
                ticketService.updateTicketStatus(id, request.status())
        );
    }
}
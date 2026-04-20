package com.mostafa.aisupport.ticket.api.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.mostafa.aisupport.ticket.api.dto.AssignAgentRequest;
import com.mostafa.aisupport.ticket.api.dto.AssignTeamRequest;
import com.mostafa.aisupport.ticket.api.dto.CreateTicketRequest;
import com.mostafa.aisupport.ticket.api.dto.TicketResponse;
import com.mostafa.aisupport.ticket.api.dto.UpdateTicketStatusRequest;
import com.mostafa.aisupport.ticket.application.TicketService;
import com.mostafa.aisupport.ticket.application.TicketWorkflowService;
import com.mostafa.aisupport.ticket.domain.entity.Ticket;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@Validated
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

    @PostMapping
    public TicketResponse createTicket(@Valid @RequestBody CreateTicketRequest request) {
        Ticket ticket = ticketWorkflowService.createTicketAndRunTriage(
                request.title(),
                request.description(),
                request.customerName(),
                request.customerEmail()
        );

        return TicketResponse.from(ticket);
    }

    @GetMapping("/{id}")
    public TicketResponse getTicketById(@PathVariable Long id) {
        return TicketResponse.from(ticketService.getTicketById(id));
    }

    @GetMapping
    public List<TicketResponse> getAllTickets() {
        return ticketService.getAllTickets()
                .stream()
                .map(TicketResponse::from)
                .toList();
    }

    @PatchMapping("/{id}/assign-team")
    public TicketResponse assignTeam(
            @PathVariable Long id,
            @Valid @RequestBody AssignTeamRequest request
    ) {
        return TicketResponse.from(
                ticketService.assignTicketToTeam(id, request.assignedTeam())
        );
    }

    @PatchMapping("/{id}/assign-agent")
    public TicketResponse assignAgent(
            @PathVariable Long id,
            @Valid @RequestBody AssignAgentRequest request
    ) {
        return TicketResponse.from(
                ticketService.assignTicketToAgent(id, request.assignedTo())
        );
    }

    @PatchMapping("/{id}/status")
    public TicketResponse updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTicketStatusRequest request
    ) {
        return TicketResponse.from(
                ticketService.updateTicketStatus(id, request.status())
        );
    }
}
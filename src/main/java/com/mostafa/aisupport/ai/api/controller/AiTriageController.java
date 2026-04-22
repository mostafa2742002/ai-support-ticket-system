package com.mostafa.aisupport.ai.api.controller;

import com.mostafa.aisupport.ai.api.dto.AiTriageResult;
import com.mostafa.aisupport.ai.application.TicketTriageAgentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/ai", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "AI Triage", description = "AI-powered ticket triage endpoints")
public class AiTriageController {

    private final TicketTriageAgentService ticketTriageAgentService;

    public AiTriageController(TicketTriageAgentService ticketTriageAgentService) {
        this.ticketTriageAgentService = ticketTriageAgentService;
    }

    @Operation(
            summary = "Manually trigger AI triage for a ticket",
            description = "Protected endpoint. Useful for re-triage, debugging, or internal admin workflows."
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/tickets/{ticketId}/triage")
    public AiTriageResult triageTicket(@PathVariable Long ticketId) {
        return ticketTriageAgentService.triageTicket(ticketId);
    }
}
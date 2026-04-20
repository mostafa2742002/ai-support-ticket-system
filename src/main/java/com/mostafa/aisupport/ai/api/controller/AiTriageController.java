package com.mostafa.aisupport.ai.api.controller;



import com.mostafa.aisupport.ai.api.dto.AiTriageResult;
import com.mostafa.aisupport.ai.application.TicketTriageAgentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AiTriageController {

    private final TicketTriageAgentService ticketTriageAgentService;

    public AiTriageController(TicketTriageAgentService ticketTriageAgentService) {
        this.ticketTriageAgentService = ticketTriageAgentService;
    }

    @PostMapping("/tickets/{ticketId}/triage")
    public AiTriageResult triageTicket(@PathVariable Long ticketId) {
        return ticketTriageAgentService.triageTicket(ticketId);
    }
}
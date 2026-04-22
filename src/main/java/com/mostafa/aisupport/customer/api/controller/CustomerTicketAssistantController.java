package com.mostafa.aisupport.customer.api.controller;


import com.mostafa.aisupport.customer.api.dto.AskTicketQuestionRequest;
import com.mostafa.aisupport.customer.api.dto.AskTicketQuestionResponse;
import com.mostafa.aisupport.customer.application.CustomerTicketAssistantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/customer/tickets", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Customer Ticket Assistant", description = "Customer-facing ticket question endpoints")
public class CustomerTicketAssistantController {

    private final CustomerTicketAssistantService customerTicketAssistantService;

    public CustomerTicketAssistantController(
            CustomerTicketAssistantService customerTicketAssistantService
    ) {
        this.customerTicketAssistantService = customerTicketAssistantService;
    }

    @Operation(
            summary = "Ask about ticket status and progress",
            description = "Public customer endpoint. Verifies ticket ownership using ticketId and customerEmail, then answers questions about the ticket."
    )
    @PostMapping(value = "/ask", consumes = MediaType.APPLICATION_JSON_VALUE)
    public AskTicketQuestionResponse askQuestion(
            @Valid @RequestBody AskTicketQuestionRequest request
    ) {
        String answer = customerTicketAssistantService.answerQuestion(
                request.ticketId(),
                request.customerEmail(),
                request.question()
        );

        return new AskTicketQuestionResponse(answer);
    }
}
package com.mostafa.aisupport.ticket.api;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.mostafa.aisupport.ticket.api.controller.TicketController;
import com.mostafa.aisupport.ticket.api.dto.CreateTicketRequest;
import com.mostafa.aisupport.ticket.application.TicketService;
import com.mostafa.aisupport.ticket.application.TicketWorkflowService;
import com.mostafa.aisupport.ticket.domain.entity.Ticket;
import com.mostafa.aisupport.ticket.domain.enums.TicketCategory;
import com.mostafa.aisupport.ticket.domain.enums.TicketPriority;
import com.mostafa.aisupport.ticket.domain.enums.TicketStatus;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TicketController.class)
class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TicketService ticketService;

    @MockBean
    private TicketWorkflowService ticketWorkflowService;

    @Test
    void shouldCreateTicketSuccessfully() throws Exception {
        Ticket ticket = new Ticket(
                1L,
                "Charged twice",
                "My card was charged twice",
                "Ahmed Mostafa",
                "ahmed@email.com",
                TicketStatus.OPEN,
                TicketCategory.BILLING,
                TicketPriority.HIGH,
                "BILLING",
                "Sara",
                "Customer charged twice for premium plan",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        CreateTicketRequest request = new CreateTicketRequest(
                "Charged twice",
                "My card was charged twice",
                "Ahmed Mostafa",
                "ahmed@email.com"
        );

        when(ticketWorkflowService.createTicketAndRunTriage(
                anyString(), anyString(), anyString(), anyString()
        )).thenReturn(ticket);

        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Charged twice"))
                .andExpect(jsonPath("$.category").value("BILLING"))
                .andExpect(jsonPath("$.assignedTo").value("Sara"));
    }

    @Test
    void shouldReturnBadRequestWhenBodyInvalid() throws Exception {
        String invalidBody = """
                {
                  "title": "",
                  "description": "",
                  "customerName": "",
                  "customerEmail": "not-an-email"
                }
                """;

        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(invalidBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }
}
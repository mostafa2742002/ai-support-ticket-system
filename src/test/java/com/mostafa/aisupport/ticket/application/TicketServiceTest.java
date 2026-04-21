package com.mostafa.aisupport.ticket.application;



import com.mostafa.aisupport.common.exception.NotFoundException;
import com.mostafa.aisupport.ticket.domain.entity.Ticket;
import com.mostafa.aisupport.ticket.domain.enums.TicketCategory;
import com.mostafa.aisupport.ticket.domain.enums.TicketPriority;
import com.mostafa.aisupport.ticket.infrastructure.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TicketServiceTest {

    private TicketRepository ticketRepository;
    private TicketService ticketService;

    @BeforeEach
    void setUp() {
        ticketRepository = mock(TicketRepository.class);
        ticketService = new TicketService(ticketRepository);
    }

    @Test
    void shouldCreateTicketWithDefaultValues() {
        when(ticketRepository.save(any(Ticket.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Ticket ticket = ticketService.createTicket(
                "Payment issue",
                "I was charged twice",
                "Mostafa",
                "mostafa@email.com"
        );

        assertThat(ticket.getTitle()).isEqualTo("Payment issue");
        assertThat(ticket.getDescription()).isEqualTo("I was charged twice");
        assertThat(ticket.getCustomerName()).isEqualTo("Mostafa");
        assertThat(ticket.getCustomerEmail()).isEqualTo("mostafa@email.com");
        assertThat(ticket.getStatus().name()).isEqualTo("OPEN");
        assertThat(ticket.getPriority().name()).isEqualTo("MEDIUM");
        assertThat(ticket.getCategory()).isNull();
        assertThat(ticket.getAssignedTeam()).isNull();
        assertThat(ticket.getAssignedTo()).isNull();
        assertThat(ticket.getAiSummary()).isNull();
    }

    @Test
    void shouldReturnTicketById() {
        Ticket ticket = Ticket.createNew(
                "Login issue",
                "I cannot login",
                "Ahmed",
                "ahmed@email.com"
        );

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        Ticket result = ticketService.getTicketById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Login issue");
    }

    @Test
    void shouldThrowWhenTicketNotFound() {
        when(ticketRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ticketService.getTicketById(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Ticket not found with id: 99");
    }

    @Test
    void shouldApplyAiTriageResult() {
        Ticket ticket = Ticket.createNew(
                "Charged twice",
                "My card was charged twice",
                "Ahmed",
                "ahmed@email.com"
        );

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(any(Ticket.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Ticket updated = ticketService.applyAiTriageResult(
                1L,
                "BILLING",
                "Customer reports duplicate charge",
                TicketCategory.BILLING,
                TicketPriority.HIGH
        );

        assertThat(updated.getCategory()).isEqualTo(TicketCategory.BILLING);
        assertThat(updated.getPriority()).isEqualTo(TicketPriority.HIGH);
        assertThat(updated.getAssignedTeam()).isEqualTo("BILLING");
        assertThat(updated.getAiSummary()).isEqualTo("Customer reports duplicate charge");
    }

    @Test
    void shouldRejectNullPriorityInAiTriage() {
        Ticket ticket = Ticket.createNew(
                "Charged twice",
                "My card was charged twice",
                "Ahmed",
                "ahmed@email.com"
        );

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        assertThatThrownBy(() ->
                ticketService.applyAiTriageResult(
                        1L,
                        "BILLING",
                        "summary",
                        TicketCategory.BILLING,
                        null
                )
        ).isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Priority must not be null");
    }
}
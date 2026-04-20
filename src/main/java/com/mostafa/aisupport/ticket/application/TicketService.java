package com.mostafa.aisupport.ticket.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mostafa.aisupport.common.exception.NotFoundException;
import com.mostafa.aisupport.ticket.domain.entity.Ticket;
import com.mostafa.aisupport.ticket.domain.enums.TicketPriority;
import com.mostafa.aisupport.ticket.domain.enums.TicketStatus;
import com.mostafa.aisupport.ticket.infrastructure.TicketRepository;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Ticket createTicket(
        String title,
        String description,
        String customerName,
        String customerEmail
            ){
        Ticket ticket = Ticket.createNew(title, description, customerName, customerEmail);

        return ticketRepository.save(ticket);
    }

    @Transactional(readOnly = true)
    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ticket not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Ticket> getTicketsByStatus(TicketStatus status) {
        return ticketRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public List<Ticket> getTicketsByPriority(TicketPriority priority) {
        return ticketRepository.findByPriority(priority);
    }

    public Ticket assignTicketToTeam(Long ticketId, String assignedTeam) {
        Ticket ticket = getTicketById(ticketId);
        ticket.assignTeam(assignedTeam);
        return ticketRepository.save(ticket);
    }

    public Ticket assignTicketToAgent(Long ticketId, String assignedTo) {
        Ticket ticket = getTicketById(ticketId);
        ticket.assignTo(assignedTo);
        return ticketRepository.save(ticket);
    }

    public Ticket updateTicketStatus(Long ticketId, TicketStatus status) {
        Ticket ticket = getTicketById(ticketId);
        ticket.updateStatus(status);
        return ticketRepository.save(ticket);
    }
}

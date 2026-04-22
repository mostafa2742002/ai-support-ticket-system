package com.mostafa.aisupport.ticket.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class TicketEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public TicketEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publishTicketCreated(Long ticketId) {
        applicationEventPublisher.publishEvent(new TicketCreatedEvent(ticketId));
    }
}
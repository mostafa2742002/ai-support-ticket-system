package com.mostafa.aisupport.common.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

@Service
public class TicketMetricsService {

    private final Counter ticketCreated;
    private final Counter triageSuccess;
    private final Counter triageFailure;

    public TicketMetricsService(MeterRegistry meterRegistry) {
        this.ticketCreated = Counter.builder("tickets.created")
                .description("Number of tickets created")
                .register(meterRegistry);

        this.triageSuccess = Counter.builder("tickets.triage.success")
                .description("Number of tickets successfully triaged by AI")
                .register(meterRegistry);

        this.triageFailure = Counter.builder("tickets.triage.failure")
                .description("Number of tickets that failed AI triage")
                .register(meterRegistry);
    }

    public void incrementTicketCreated() {
        ticketCreated.increment();
    }

    public void incrementTriageSuccess() {
        triageSuccess.increment();
    }

    public void incrementTriageFailure() {
        triageFailure.increment();
    }

}

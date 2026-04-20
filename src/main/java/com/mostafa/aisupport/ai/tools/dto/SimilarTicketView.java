package com.mostafa.aisupport.ai.tools.dto;

public record SimilarTicketView(
        Long id,
        String title,
        String category,
        String priority,
        String assignedTeam
) {
}
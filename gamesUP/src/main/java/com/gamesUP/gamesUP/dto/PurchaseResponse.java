package com.gamesUP.gamesUP.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO de réponse renvoyé pour les commandes.
 */
public record PurchaseResponse(
        Long id,
        ReferenceResponse user,
        List<PurchaseLineResponse> lines,
        LocalDateTime date,
        boolean paid,
        boolean delivered,
        boolean archived
) {
}

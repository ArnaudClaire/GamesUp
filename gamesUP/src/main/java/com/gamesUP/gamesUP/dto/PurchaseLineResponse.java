package com.gamesUP.gamesUP.dto;

import java.math.BigDecimal;

/**
 * DTO de réponse représentant une ligne de commande.
 */
public record PurchaseLineResponse(
        Long id,
        Long gameId,
        String gameName,
        int quantity,
        BigDecimal unitPrice
) {
}

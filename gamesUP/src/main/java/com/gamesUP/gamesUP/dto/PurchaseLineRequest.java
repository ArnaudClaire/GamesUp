package com.gamesUP.gamesUP.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * DTO de requête représentant une ligne de commande.
 */
public record PurchaseLineRequest(
        @NotNull
        Long gameId,
        @Min(1)
        int quantity
) {
}

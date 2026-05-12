package com.gamesUP.gamesUP.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * DTO de requête utilisé pour créer ou modifier les quantités en stock.
 */
public record InventoryRequest(
        @NotNull
        Long gameId,
        @Min(0)
        int quantity
) {
}

package com.gamesUP.gamesUP.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO used to create or update stock quantities.
 */
public record InventoryRequest(
        @NotNull
        Long gameId,
        @Min(0)
        int quantity
) {
}

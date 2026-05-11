package com.gamesUP.gamesUP.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO representing one purchase line.
 */
public record PurchaseLineRequest(
        @NotNull
        Long gameId,
        @Min(1)
        int quantity
) {
}

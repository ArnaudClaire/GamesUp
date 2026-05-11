package com.gamesUP.gamesUP.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PurchaseLineRequest(
        @NotNull
        Long gameId,
        @Min(1)
        int quantity
) {
}

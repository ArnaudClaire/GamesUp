package com.gamesUP.gamesUP.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * DTO de requête utilisé pour créer un avis.
 */
public record ReviewRequest(
        @NotNull
        Long userId,
        @NotNull
        Long gameId,
        @Min(1)
        @Max(5)
        int rating,
        String comment
) {
}

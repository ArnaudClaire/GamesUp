package com.gamesUP.gamesUP.dto;

import java.math.BigDecimal;
import java.util.Set;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO de requête utilisé pour créer ou modifier un jeu.
 */
public record GameRequest(
        @NotBlank
        String name,
        String description,
        String genre,
        @Min(1)
        Integer minPlayers,
        @Min(1)
        Integer maxPlayers,
        @Min(1)
        Integer durationMinutes,
        @Min(1)
        Integer editionNumber,
        @DecimalMin("0.0")
        BigDecimal price,
        Long categoryId,
        Long publisherId,
        Set<Long> authorIds
) {
}

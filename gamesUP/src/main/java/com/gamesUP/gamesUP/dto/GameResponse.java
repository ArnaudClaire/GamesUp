package com.gamesUP.gamesUP.dto;

import java.math.BigDecimal;
import java.util.Set;

/**
 * Response DTO returned for game catalogue entries.
 */
public record GameResponse(
        Long id,
        String name,
        String description,
        String genre,
        Integer minPlayers,
        Integer maxPlayers,
        Integer durationMinutes,
        Integer editionNumber,
        BigDecimal price,
        ReferenceResponse category,
        ReferenceResponse publisher,
        Set<ReferenceResponse> authors,
        Integer stockQuantity
) {
}

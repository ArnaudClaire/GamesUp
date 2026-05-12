package com.gamesUP.gamesUP.dto;

import jakarta.validation.constraints.NotNull;
import java.util.Set;

/**
 * DTO de requête utilisé pour créer ou modifier une liste d'envies.
 */
public record WishlistRequest(
        @NotNull
        Long userId,
        Set<Long> gameIds
) {
}

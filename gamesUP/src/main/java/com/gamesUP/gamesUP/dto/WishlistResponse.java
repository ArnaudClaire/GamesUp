package com.gamesUP.gamesUP.dto;

import java.util.Set;

/**
 * DTO de réponse renvoyé pour les listes d'envies.
 */
public record WishlistResponse(
        Long id,
        ReferenceResponse user,
        Set<ReferenceResponse> games
) {
}

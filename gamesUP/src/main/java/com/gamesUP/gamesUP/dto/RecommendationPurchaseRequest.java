package com.gamesUP.gamesUP.dto;

/**
 * DTO portant un signal de recommandation envoyé à l'API Python.
 */
public record RecommendationPurchaseRequest(
        Long gameId,
        double rating
) {
}

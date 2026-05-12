package com.gamesUP.gamesUP.dto;

/**
 * DTO de réponse renvoyé par l'API de recommandation.
 */
public record RecommendationResponse(
        Long gameId,
        String gameName,
        double score,
        String reason
) {
}

package com.gamesUP.gamesUP.dto;

/**
 * Response DTO returned by the recommendation API.
 */
public record RecommendationResponse(
        Long gameId,
        String gameName,
        double score,
        String reason
) {
}

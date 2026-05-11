package com.gamesUP.gamesUP.dto;

public record RecommendationResponse(
        Long gameId,
        String gameName,
        double score,
        String reason
) {
}

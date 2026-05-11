package com.gamesUP.gamesUP.dto;

public record RecommendationPurchaseRequest(
        Long gameId,
        double rating
) {
}

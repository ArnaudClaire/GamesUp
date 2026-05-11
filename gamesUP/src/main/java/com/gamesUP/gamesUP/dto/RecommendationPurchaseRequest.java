package com.gamesUP.gamesUP.dto;

/**
 * DTO carrying one recommendation signal sent to the Python API.
 */
public record RecommendationPurchaseRequest(
        Long gameId,
        double rating
) {
}

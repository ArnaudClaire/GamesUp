package com.gamesUP.gamesUP.dto;

import java.util.List;

/**
 * Request DTO sent to the recommendation API.
 */
public record RecommendationRequest(
        Long userId,
        List<RecommendationPurchaseRequest> purchases
) {
}

package com.gamesUP.gamesUP.dto;

import java.util.List;

public record RecommendationRequest(
        Long userId,
        List<RecommendationPurchaseRequest> purchases
) {
}

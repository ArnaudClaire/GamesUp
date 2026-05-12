package com.gamesUP.gamesUP.dto;

import java.util.List;

/**
 * DTO de requête envoyé à l'API de recommandation.
 */
public record RecommendationRequest(
        Long userId,
        List<RecommendationPurchaseRequest> purchases
) {
}

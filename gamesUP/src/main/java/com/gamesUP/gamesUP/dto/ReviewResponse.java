package com.gamesUP.gamesUP.dto;

/**
 * DTO de réponse renvoyé pour les avis.
 */
public record ReviewResponse(
        Long id,
        Long userId,
        String userEmail,
        Long gameId,
        String gameName,
        int rating,
        String comment
) {
}

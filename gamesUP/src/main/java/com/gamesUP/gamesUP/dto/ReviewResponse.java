package com.gamesUP.gamesUP.dto;

/**
 * Response DTO returned for reviews.
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

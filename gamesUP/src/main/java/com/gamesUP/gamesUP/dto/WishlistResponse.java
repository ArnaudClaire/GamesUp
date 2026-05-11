package com.gamesUP.gamesUP.dto;

import java.util.Set;

/**
 * Response DTO returned for wishlists.
 */
public record WishlistResponse(
        Long id,
        ReferenceResponse user,
        Set<ReferenceResponse> games
) {
}

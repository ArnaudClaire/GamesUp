package com.gamesUP.gamesUP.dto;

import jakarta.validation.constraints.NotNull;
import java.util.Set;

/**
 * Request DTO used to create or update a wishlist.
 */
public record WishlistRequest(
        @NotNull
        Long userId,
        Set<Long> gameIds
) {
}

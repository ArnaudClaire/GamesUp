package com.gamesUP.gamesUP.dto;

/**
 * Response DTO returned for stock quantities.
 */
public record InventoryResponse(
        Long id,
        Long gameId,
        String gameName,
        int quantity
) {
}

package com.gamesUP.gamesUP.dto;

public record InventoryResponse(
        Long id,
        Long gameId,
        String gameName,
        int quantity
) {
}

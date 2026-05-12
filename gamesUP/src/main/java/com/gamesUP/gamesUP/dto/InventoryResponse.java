package com.gamesUP.gamesUP.dto;

/**
 * DTO de réponse renvoyé pour les quantités en stock.
 */
public record InventoryResponse(
        Long id,
        Long gameId,
        String gameName,
        int quantity
) {
}

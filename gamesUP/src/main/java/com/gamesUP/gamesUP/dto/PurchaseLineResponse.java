package com.gamesUP.gamesUP.dto;

import java.math.BigDecimal;

/**
 * Response DTO representing one purchase line.
 */
public record PurchaseLineResponse(
        Long id,
        Long gameId,
        String gameName,
        int quantity,
        BigDecimal unitPrice
) {
}

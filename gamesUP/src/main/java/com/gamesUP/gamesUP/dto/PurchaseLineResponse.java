package com.gamesUP.gamesUP.dto;

import java.math.BigDecimal;

public record PurchaseLineResponse(
        Long id,
        Long gameId,
        String gameName,
        int quantity,
        BigDecimal unitPrice
) {
}

package com.gamesUP.gamesUP.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * DTO de requête utilisé pour créer une commande.
 */
public record PurchaseRequest(
        @NotNull
        Long userId,
        @Valid
        @NotEmpty
        List<PurchaseLineRequest> lines
) {
}

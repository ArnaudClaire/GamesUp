package com.gamesUP.gamesUP.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * Request DTO used to create a purchase.
 */
public record PurchaseRequest(
        @NotNull
        Long userId,
        @Valid
        @NotEmpty
        List<PurchaseLineRequest> lines
) {
}

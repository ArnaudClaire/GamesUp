package com.gamesUP.gamesUP.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record PurchaseRequest(
        @NotNull
        Long userId,
        @Valid
        @NotEmpty
        List<PurchaseLineRequest> lines
) {
}

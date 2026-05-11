package com.gamesUP.gamesUP.dto;

import java.time.LocalDateTime;
import java.util.List;

public record PurchaseResponse(
        Long id,
        ReferenceResponse user,
        List<PurchaseLineResponse> lines,
        LocalDateTime date,
        boolean paid,
        boolean delivered,
        boolean archived
) {
}

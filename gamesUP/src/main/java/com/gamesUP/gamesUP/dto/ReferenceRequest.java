package com.gamesUP.gamesUP.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO de requête utilisé pour créer ou modifier une donnée de référence.
 */
public record ReferenceRequest(
        @NotBlank
        String name
) {
}

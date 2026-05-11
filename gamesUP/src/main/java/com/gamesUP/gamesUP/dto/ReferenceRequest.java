package com.gamesUP.gamesUP.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO used to create or update reference data.
 */
public record ReferenceRequest(
        @NotBlank
        String name
) {
}

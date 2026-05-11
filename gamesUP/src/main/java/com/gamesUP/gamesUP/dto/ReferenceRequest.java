package com.gamesUP.gamesUP.dto;

import jakarta.validation.constraints.NotBlank;

public record ReferenceRequest(
        @NotBlank
        String name
) {
}

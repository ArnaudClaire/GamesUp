package com.gamesUP.gamesUP.dto;

import com.gamesUP.gamesUP.model.Role;

/**
 * DTO de réponse renvoyé pour les comptes utilisateur.
 */
public record UserResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        Role role
) {
}

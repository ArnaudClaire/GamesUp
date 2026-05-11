package com.gamesUP.gamesUP.dto;

import com.gamesUP.gamesUP.model.Role;

/**
 * Response DTO returned for user accounts.
 */
public record UserResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        Role role
) {
}

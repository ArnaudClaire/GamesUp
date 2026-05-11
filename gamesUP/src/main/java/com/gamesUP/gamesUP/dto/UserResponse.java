package com.gamesUP.gamesUP.dto;

import com.gamesUP.gamesUP.model.Role;

public record UserResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        Role role
) {
}

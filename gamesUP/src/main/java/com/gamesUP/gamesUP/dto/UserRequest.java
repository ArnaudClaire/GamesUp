package com.gamesUP.gamesUP.dto;

import com.gamesUP.gamesUP.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest(
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @Email
        @NotBlank
        String email,
        @Size(min = 6)
        String password,
        Role role
) {
}

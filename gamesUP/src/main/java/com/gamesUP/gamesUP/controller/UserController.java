package com.gamesUP.gamesUP.controller;

import com.gamesUP.gamesUP.dto.UserRequest;
import com.gamesUP.gamesUP.dto.UserResponse;
import com.gamesUP.gamesUP.service.UserService;
import jakarta.validation.Valid;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
/**
 * Exposes REST endpoints for user accounts.
 */
public class UserController {

    private final UserService userService;

    /**
     * Creates the controller with the user service dependency.
     *
     * @param userService service responsible for user operations
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Lists every user account.
     *
     * @return users
     */
    @GetMapping
    public List<UserResponse> findAll() {
        return userService.findAll();
    }

    /**
     * Returns the authenticated user's account.
     *
     * @param principal authenticated user principal
     * @return current user
     */
    @GetMapping("/me")
    public UserResponse me(Principal principal) {
        return userService.findByEmail(principal.getName());
    }

    /**
     * Finds a user by identifier.
     *
     * @param id user identifier
     * @return requested user
     */
    @GetMapping("/{id}")
    public UserResponse findById(@PathVariable Long id) {
        return userService.findById(id);
    }

    /**
     * Creates a client account.
     *
     * @param request validated user payload
     * @return created user response with its location
     */
    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest request) {
        UserResponse response = userService.create(request);
        return ResponseEntity.created(URI.create("/api/users/" + response.id())).body(response);
    }

    /**
     * Updates a user account.
     *
     * @param id user identifier
     * @param request validated user payload
     * @return updated user
     */
    @PutMapping("/{id}")
    public UserResponse update(@PathVariable Long id, @Valid @RequestBody UserRequest request) {
        return userService.update(id, request);
    }

    /**
     * Deletes a user account.
     *
     * @param id user identifier
     * @return empty response when the deletion succeeds
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

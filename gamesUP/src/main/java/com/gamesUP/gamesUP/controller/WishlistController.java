package com.gamesUP.gamesUP.controller;

import com.gamesUP.gamesUP.dto.WishlistRequest;
import com.gamesUP.gamesUP.dto.WishlistResponse;
import com.gamesUP.gamesUP.service.WishlistService;
import jakarta.validation.Valid;
import java.net.URI;
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

/**
 * Exposes REST endpoints for user wishlists.
 */
@RestController
@RequestMapping("/api/wishlists")
public class WishlistController {

    private final WishlistService wishlistService;

    /**
     * Creates the controller with the wishlist service dependency.
     *
     * @param wishlistService service responsible for wishlist operations
     */
    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    /**
     * Lists every wishlist.
     *
     * @return wishlists
     */
    @GetMapping
    public List<WishlistResponse> findAll() {
        return wishlistService.findAll();
    }

    /**
     * Finds a wishlist by identifier.
     *
     * @param id wishlist identifier
     * @return requested wishlist
     */
    @GetMapping("/{id}")
    public WishlistResponse findById(@PathVariable Long id) {
        return wishlistService.findById(id);
    }

    /**
     * Finds the wishlist owned by one user.
     *
     * @param userId user identifier
     * @return requested wishlist
     */
    @GetMapping("/users/{userId}")
    public WishlistResponse findByUser(@PathVariable Long userId) {
        return wishlistService.findByUser(userId);
    }

    /**
     * Creates a wishlist.
     *
     * @param request validated wishlist payload
     * @return created wishlist response with its location
     */
    @PostMapping
    public ResponseEntity<WishlistResponse> create(@Valid @RequestBody WishlistRequest request) {
        WishlistResponse response = wishlistService.create(request);
        return ResponseEntity.created(URI.create("/api/wishlists/" + response.id())).body(response);
    }

    /**
     * Updates a wishlist.
     *
     * @param id wishlist identifier
     * @param request validated wishlist payload
     * @return updated wishlist
     */
    @PutMapping("/{id}")
    public WishlistResponse update(@PathVariable Long id, @Valid @RequestBody WishlistRequest request) {
        return wishlistService.update(id, request);
    }

    /**
     * Deletes a wishlist.
     *
     * @param id wishlist identifier
     * @return empty response when the deletion succeeds
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        wishlistService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

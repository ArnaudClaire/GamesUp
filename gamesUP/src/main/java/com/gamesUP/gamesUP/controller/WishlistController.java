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
 * Expose les endpoints REST liés aux listes d'envies des utilisateurs.
 */
@RestController
@RequestMapping("/api/wishlists")
public class WishlistController {

    private final WishlistService wishlistService;

    /**
     * Crée le contrôleur avec sa dépendance vers le service des listes d'envies.
     *
     * @param wishlistService service responsable des opérations liées aux listes d'envies
     */
    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    /**
     * Liste toutes les listes d'envies.
     *
     * @return listes d'envies
     */
    @GetMapping
    public List<WishlistResponse> findAll() {
        return wishlistService.findAll();
    }

    /**
     * Recherche une liste d'envies par son identifiant.
     *
     * @param id identifiant de la liste d'envies
     * @return liste d'envies demandée
     */
    @GetMapping("/{id}")
    public WishlistResponse findById(@PathVariable Long id) {
        return wishlistService.findById(id);
    }

    /**
     * Recherche la liste d'envies possédée par un utilisateur.
     *
     * @param userId identifiant utilisateur
     * @return liste d'envies demandée
     */
    @GetMapping("/users/{userId}")
    public WishlistResponse findByUser(@PathVariable Long userId) {
        return wishlistService.findByUser(userId);
    }

    /**
     * Crée une liste d'envies.
     *
     * @param request données de liste d'envies validées
     * @return réponse de la liste d'envies créée avec son emplacement
     */
    @PostMapping
    public ResponseEntity<WishlistResponse> create(@Valid @RequestBody WishlistRequest request) {
        WishlistResponse response = wishlistService.create(request);
        return ResponseEntity.created(URI.create("/api/wishlists/" + response.id())).body(response);
    }

    /**
     * Met à jour une liste d'envies.
     *
     * @param id identifiant de la liste d'envies
     * @param request données de liste d'envies validées
     * @return liste d'envies mise à jour
     */
    @PutMapping("/{id}")
    public WishlistResponse update(@PathVariable Long id, @Valid @RequestBody WishlistRequest request) {
        return wishlistService.update(id, request);
    }

    /**
     * Supprime une liste d'envies.
     *
     * @param id identifiant de la liste d'envies
     * @return réponse vide lorsque la suppression réussit
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        wishlistService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

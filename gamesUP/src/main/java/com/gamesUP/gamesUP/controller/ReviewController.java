package com.gamesUP.gamesUP.controller;

import com.gamesUP.gamesUP.dto.ReviewRequest;
import com.gamesUP.gamesUP.dto.ReviewResponse;
import com.gamesUP.gamesUP.service.ReviewService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reviews")
/**
 * Expose les endpoints REST liés aux avis clients.
 */
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * Crée le contrôleur avec sa dépendance vers le service des avis.
     *
     * @param reviewService service responsable des opérations liées aux avis
     */
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /**
     * Liste les avis, avec un filtre optionnel par jeu ou utilisateur.
     *
     * @param gameId identifiant de jeu optionnel
     * @param userId identifiant utilisateur optionnel
     * @return avis correspondants
     */
    @GetMapping
    public List<ReviewResponse> findAll(
            @RequestParam(required = false) Long gameId,
            @RequestParam(required = false) Long userId
    ) {
        return reviewService.findAll(gameId, userId);
    }

    /**
     * Crée un avis.
     *
     * @param request données d'avis validées
     * @return réponse de l'avis créé avec son emplacement
     */
    @PostMapping
    public ResponseEntity<ReviewResponse> create(@Valid @RequestBody ReviewRequest request) {
        ReviewResponse response = reviewService.create(request);
        return ResponseEntity.created(URI.create("/api/reviews/" + response.id())).body(response);
    }

    /**
     * Supprime un avis.
     *
     * @param id identifiant de l'avis
     * @return réponse vide lorsque la suppression réussit
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reviewService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

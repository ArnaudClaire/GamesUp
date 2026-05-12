package com.gamesUP.gamesUP.controller;

import com.gamesUP.gamesUP.dto.RecommendationRequest;
import com.gamesUP.gamesUP.dto.RecommendationResponse;
import com.gamesUP.gamesUP.service.RecommendationService;
import com.gamesUP.gamesUP.service.UserService;
import java.security.Principal;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recommendations")
/**
 * Expose les endpoints REST utilisés pour demander des recommandations de jeux.
 */
public class RecommendationController {

    private final RecommendationService recommendationService;
    private final UserService userService;

    /**
     * Crée le contrôleur avec les services de recommandation et d'utilisateurs.
     *
     * @param recommendationService service responsable des appels de recommandation
     * @param userService service utilisé pour résoudre l'utilisateur authentifié
     */
    public RecommendationController(RecommendationService recommendationService, UserService userService) {
        this.recommendationService = recommendationService;
        this.userService = userService;
    }

    /**
     * Demande les recommandations de l'utilisateur authentifié.
     *
     * @param principal principal de l'utilisateur authentifié
     * @return recommandations renvoyées par l'API Python
     */
    @GetMapping("/me")
    public List<RecommendationResponse> recommendForCurrentUser(Principal principal) {
        return recommendationService.recommendForUser(userService.findByEmail(principal.getName()).id());
    }

    /**
     * Demande les recommandations d'un utilisateur sélectionné par un administrateur.
     *
     * @param userId identifiant utilisateur
     * @return recommandations renvoyées par l'API Python
     */
    @GetMapping("/users/{userId}")
    public List<RecommendationResponse> recommendForUser(@PathVariable Long userId) {
        return recommendationService.recommendForUser(userId);
    }

    /**
     * Envoie des signaux de recommandation explicites à l'API Python.
     *
     * @param request données de recommandation
     * @return recommandations renvoyées par l'API Python
     */
    @PostMapping
    public List<RecommendationResponse> recommend(@RequestBody RecommendationRequest request) {
        return recommendationService.recommend(request);
    }
}

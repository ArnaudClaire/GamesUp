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
 * Exposes REST endpoints used to request game recommendations.
 */
public class RecommendationController {

    private final RecommendationService recommendationService;
    private final UserService userService;

    /**
     * Creates the controller with recommendation and user services.
     *
     * @param recommendationService service responsible for recommendation calls
     * @param userService service used to resolve the authenticated user
     */
    public RecommendationController(RecommendationService recommendationService, UserService userService) {
        this.recommendationService = recommendationService;
        this.userService = userService;
    }

    /**
     * Requests recommendations for the authenticated user.
     *
     * @param principal authenticated user principal
     * @return recommendations returned by the Python API
     */
    @GetMapping("/me")
    public List<RecommendationResponse> recommendForCurrentUser(Principal principal) {
        return recommendationService.recommendForUser(userService.findByEmail(principal.getName()).id());
    }

    /**
     * Requests recommendations for a user selected by an administrator.
     *
     * @param userId user identifier
     * @return recommendations returned by the Python API
     */
    @GetMapping("/users/{userId}")
    public List<RecommendationResponse> recommendForUser(@PathVariable Long userId) {
        return recommendationService.recommendForUser(userId);
    }

    /**
     * Sends explicit recommendation signals to the Python API.
     *
     * @param request recommendation payload
     * @return recommendations returned by the Python API
     */
    @PostMapping
    public List<RecommendationResponse> recommend(@RequestBody RecommendationRequest request) {
        return recommendationService.recommend(request);
    }
}

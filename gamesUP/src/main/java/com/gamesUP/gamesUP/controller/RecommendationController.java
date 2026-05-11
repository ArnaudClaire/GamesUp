package com.gamesUP.gamesUP.controller;

import com.gamesUP.gamesUP.dto.RecommendationRequest;
import com.gamesUP.gamesUP.dto.RecommendationResponse;
import com.gamesUP.gamesUP.service.RecommendationService;
import com.gamesUP.gamesUP.service.UserService;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;
    private final UserService userService;

    @GetMapping("/me")
    public List<RecommendationResponse> recommendForCurrentUser(Principal principal) {
        return recommendationService.recommendForUser(userService.findByEmail(principal.getName()).id());
    }

    @GetMapping("/users/{userId}")
    public List<RecommendationResponse> recommendForUser(@org.springframework.web.bind.annotation.PathVariable Long userId) {
        return recommendationService.recommendForUser(userId);
    }

    @PostMapping
    public List<RecommendationResponse> recommend(@RequestBody RecommendationRequest request) {
        return recommendationService.recommend(request);
    }
}

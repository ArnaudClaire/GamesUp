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
 * Exposes REST endpoints for customer reviews.
 */
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * Creates the controller with the review service dependency.
     *
     * @param reviewService service responsible for review operations
     */
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /**
     * Lists reviews, optionally filtered by game or user.
     *
     * @param gameId optional game identifier
     * @param userId optional user identifier
     * @return matching reviews
     */
    @GetMapping
    public List<ReviewResponse> findAll(
            @RequestParam(required = false) Long gameId,
            @RequestParam(required = false) Long userId
    ) {
        return reviewService.findAll(gameId, userId);
    }

    /**
     * Creates a review.
     *
     * @param request validated review payload
     * @return created review response with its location
     */
    @PostMapping
    public ResponseEntity<ReviewResponse> create(@Valid @RequestBody ReviewRequest request) {
        ReviewResponse response = reviewService.create(request);
        return ResponseEntity.created(URI.create("/api/reviews/" + response.id())).body(response);
    }

    /**
     * Deletes a review.
     *
     * @param id review identifier
     * @return empty response when the deletion succeeds
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reviewService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

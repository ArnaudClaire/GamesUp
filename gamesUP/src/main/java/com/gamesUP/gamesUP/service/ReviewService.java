package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.dto.ReviewRequest;
import com.gamesUP.gamesUP.dto.ReviewResponse;
import com.gamesUP.gamesUP.exception.ResourceNotFoundException;
import com.gamesUP.gamesUP.model.Avis;
import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.model.User;
import com.gamesUP.gamesUP.repository.AvisRepository;
import com.gamesUP.gamesUP.repository.GameRepository;
import com.gamesUP.gamesUP.repository.UserRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
/**
 * Provides business operations for customer reviews.
 */
public class ReviewService {

    private final AvisRepository avisRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;

    /**
     * Creates the service with its repository dependencies.
     *
     * @param avisRepository repository for reviews
     * @param userRepository repository for users
     * @param gameRepository repository for games
     */
    public ReviewService(
            AvisRepository avisRepository,
            UserRepository userRepository,
            GameRepository gameRepository
    ) {
        this.avisRepository = avisRepository;
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
    }

    /**
     * Lists reviews, optionally filtered by game or user.
     *
     * @param gameId optional game identifier
     * @param userId optional user identifier
     * @return matching reviews
     */
    public List<ReviewResponse> findAll(Long gameId, Long userId) {
        if (gameId != null) {
            return avisRepository.findByGameId(gameId).stream().map(this::toResponse).toList();
        }
        if (userId != null) {
            return avisRepository.findByUserId(userId).stream().map(this::toResponse).toList();
        }
        return avisRepository.findAll().stream().map(this::toResponse).toList();
    }

    /**
     * Creates a review for a user and game.
     *
     * @param request review payload
     * @return created review
     */
    @Transactional
    public ReviewResponse create(ReviewRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + request.userId()));
        Game game = gameRepository.findById(request.gameId())
                .orElseThrow(() -> new ResourceNotFoundException("Game not found with id " + request.gameId()));

        Avis avis = new Avis();
        avis.setUser(user);
        avis.setGame(game);
        avis.setRating(request.rating());
        avis.setComment(request.comment());
        return toResponse(avisRepository.save(avis));
    }

    /**
     * Deletes a review.
     *
     * @param id review identifier
     */
    @Transactional
    public void delete(Long id) {
        Avis avis = avisRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id " + id));
        avisRepository.delete(avis);
    }

    private ReviewResponse toResponse(Avis avis) {
        return new ReviewResponse(
                avis.getId(),
                avis.getUser().getId(),
                avis.getUser().getEmail(),
                avis.getGame().getId(),
                avis.getGame().getName(),
                avis.getRating(),
                avis.getComment()
        );
    }
}

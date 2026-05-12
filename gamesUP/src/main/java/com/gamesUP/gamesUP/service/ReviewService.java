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
 * Fournit les opérations métier liées aux avis clients.
 */
public class ReviewService {

    private final AvisRepository avisRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;

    /**
     * Crée le service avec ses dépendances vers les dépôts.
     *
     * @param avisRepository dépôt des avis
     * @param userRepository dépôt des utilisateurs
     * @param gameRepository dépôt des jeux
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
     * Liste les avis, avec un filtre optionnel par jeu ou utilisateur.
     *
     * @param gameId identifiant de jeu optionnel
     * @param userId identifiant utilisateur optionnel
     * @return avis correspondants
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
     * Crée un avis pour un utilisateur et un jeu.
     *
     * @param request données de l'avis
     * @return avis créé
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
     * Supprime un avis.
     *
     * @param id identifiant de l'avis
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

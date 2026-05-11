package com.gamesUP.gamesUP.repository;

import com.gamesUP.gamesUP.model.Avis;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data repository for reviews.
 */
public interface AvisRepository extends JpaRepository<Avis, Long> {

    /**
     * Finds reviews for one game.
     *
     * @param gameId game identifier
     * @return matching reviews
     */
    List<Avis> findByGameId(Long gameId);

    /**
     * Finds reviews written by one user.
     *
     * @param userId user identifier
     * @return matching reviews
     */
    List<Avis> findByUserId(Long userId);
}

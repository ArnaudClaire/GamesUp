package com.gamesUP.gamesUP.repository;

import com.gamesUP.gamesUP.model.Avis;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Dépôt Spring Data pour les avis.
 */
public interface AvisRepository extends JpaRepository<Avis, Long> {

    /**
     * Recherche les avis associés à un jeu.
     *
     * @param gameId identifiant du jeu
     * @return avis correspondants
     */
    List<Avis> findByGameId(Long gameId);

    /**
     * Recherche les avis rédigés par un utilisateur.
     *
     * @param userId identifiant utilisateur
     * @return avis correspondants
     */
    List<Avis> findByUserId(Long userId);
}

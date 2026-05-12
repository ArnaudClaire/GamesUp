package com.gamesUP.gamesUP.repository;

import com.gamesUP.gamesUP.model.Wishlist;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Dépôt Spring Data pour les listes d'envies.
 */
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    /**
     * Recherche la liste d'envies possédée par un utilisateur.
     *
     * @param userId identifiant utilisateur
     * @return liste d'envies correspondante, si elle existe
     */
    Optional<Wishlist> findByUserId(Long userId);
}

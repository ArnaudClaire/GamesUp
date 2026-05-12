package com.gamesUP.gamesUP.repository;

import com.gamesUP.gamesUP.model.Purchase;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Dépôt Spring Data pour les commandes.
 */
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    /**
     * Recherche les commandes passées par un utilisateur.
     *
     * @param userId identifiant utilisateur
     * @return commandes correspondantes
     */
    List<Purchase> findByUserId(Long userId);
}

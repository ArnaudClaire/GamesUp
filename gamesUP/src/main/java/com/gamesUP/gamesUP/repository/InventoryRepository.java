package com.gamesUP.gamesUP.repository;

import com.gamesUP.gamesUP.model.Inventory;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Dépôt Spring Data pour les entrées de stock.
 */
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    /**
     * Recherche l'entrée de stock associée à un jeu.
     *
     * @param gameId identifiant du jeu
     * @return entrée de stock correspondante, si elle existe
     */
    Optional<Inventory> findByGameId(Long gameId);
}

package com.gamesUP.gamesUP.repository;

import com.gamesUP.gamesUP.model.Inventory;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data repository for inventory entries.
 */
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    /**
     * Finds the stock entry attached to one game.
     *
     * @param gameId game identifier
     * @return matching stock entry when present
     */
    Optional<Inventory> findByGameId(Long gameId);
}

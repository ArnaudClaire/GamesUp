package com.gamesUP.gamesUP.repository;

import com.gamesUP.gamesUP.model.Inventory;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByGameId(Long gameId);
}

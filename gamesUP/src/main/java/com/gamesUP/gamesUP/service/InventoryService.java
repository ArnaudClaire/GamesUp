package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.dto.InventoryRequest;
import com.gamesUP.gamesUP.dto.InventoryResponse;
import com.gamesUP.gamesUP.exception.ResourceNotFoundException;
import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.model.Inventory;
import com.gamesUP.gamesUP.repository.GameRepository;
import com.gamesUP.gamesUP.repository.InventoryRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
/**
 * Provides business operations for stock quantities.
 */
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final GameRepository gameRepository;

    /**
     * Creates the service with its repository dependencies.
     *
     * @param inventoryRepository repository for inventory entries
     * @param gameRepository repository for games
     */
    public InventoryService(InventoryRepository inventoryRepository, GameRepository gameRepository) {
        this.inventoryRepository = inventoryRepository;
        this.gameRepository = gameRepository;
    }

    /**
     * Lists all inventory entries.
     *
     * @return current inventory entries
     */
    public List<InventoryResponse> findAll() {
        return inventoryRepository.findAll().stream().map(this::toResponse).toList();
    }

    /**
     * Creates or updates the stock entry attached to a game.
     *
     * @param request inventory payload
     * @return updated inventory entry
     */
    @Transactional
    public InventoryResponse upsert(InventoryRequest request) {
        Game game = gameRepository.findById(request.gameId())
                .orElseThrow(() -> new ResourceNotFoundException("Game not found with id " + request.gameId()));

        Inventory inventory = inventoryRepository.findByGameId(request.gameId()).orElseGet(Inventory::new);
        inventory.setGame(game);
        inventory.setQuantity(request.quantity());
        return toResponse(inventoryRepository.save(inventory));
    }

    private InventoryResponse toResponse(Inventory inventory) {
        Game game = inventory.getGame();
        return new InventoryResponse(inventory.getId(), game.getId(), game.getName(), inventory.getQuantity());
    }
}

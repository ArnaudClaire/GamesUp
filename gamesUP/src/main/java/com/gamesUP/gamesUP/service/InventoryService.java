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
 * Fournit les opérations métier liées aux quantités en stock.
 */
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final GameRepository gameRepository;

    /**
     * Crée le service avec ses dépendances vers les dépôts.
     *
     * @param inventoryRepository dépôt des entrées de stock
     * @param gameRepository dépôt des jeux
     */
    public InventoryService(InventoryRepository inventoryRepository, GameRepository gameRepository) {
        this.inventoryRepository = inventoryRepository;
        this.gameRepository = gameRepository;
    }

    /**
     * Liste toutes les entrées de stock.
     *
     * @return entrées de stock actuelles
     */
    public List<InventoryResponse> findAll() {
        return inventoryRepository.findAll().stream().map(this::toResponse).toList();
    }

    /**
     * Crée ou met à jour l'entrée de stock associée à un jeu.
     *
     * @param request données de stock
     * @return entrée de stock mise à jour
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

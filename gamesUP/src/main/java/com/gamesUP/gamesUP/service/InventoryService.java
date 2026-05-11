package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.dto.InventoryRequest;
import com.gamesUP.gamesUP.dto.InventoryResponse;
import com.gamesUP.gamesUP.exception.ResourceNotFoundException;
import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.model.Inventory;
import com.gamesUP.gamesUP.repository.GameRepository;
import com.gamesUP.gamesUP.repository.InventoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final GameRepository gameRepository;

    public List<InventoryResponse> findAll() {
        return inventoryRepository.findAll().stream().map(this::toResponse).toList();
    }

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

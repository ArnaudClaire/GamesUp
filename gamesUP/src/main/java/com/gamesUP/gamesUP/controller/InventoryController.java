package com.gamesUP.gamesUP.controller;

import com.gamesUP.gamesUP.dto.InventoryRequest;
import com.gamesUP.gamesUP.dto.InventoryResponse;
import com.gamesUP.gamesUP.service.InventoryService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inventory")
/**
 * Exposes REST endpoints for stock quantities.
 */
public class InventoryController {

    private final InventoryService inventoryService;

    /**
     * Creates the controller with the inventory service dependency.
     *
     * @param inventoryService service responsible for inventory operations
     */
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    /**
     * Lists all inventory entries.
     *
     * @return current stock entries
     */
    @GetMapping
    public List<InventoryResponse> findAll() {
        return inventoryService.findAll();
    }

    /**
     * Creates or updates a stock entry for a game.
     *
     * @param request validated inventory payload
     * @return updated inventory entry
     */
    @PutMapping
    public InventoryResponse upsert(@Valid @RequestBody InventoryRequest request) {
        return inventoryService.upsert(request);
    }
}

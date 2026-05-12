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
 * Expose les endpoints REST liés aux quantités en stock.
 */
public class InventoryController {

    private final InventoryService inventoryService;

    /**
     * Crée le contrôleur avec sa dépendance vers le service de stock.
     *
     * @param inventoryService service responsable des opérations de stock
     */
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    /**
     * Liste toutes les entrées de stock.
     *
     * @return entrées de stock actuelles
     */
    @GetMapping
    public List<InventoryResponse> findAll() {
        return inventoryService.findAll();
    }

    /**
     * Crée ou met à jour une entrée de stock pour un jeu.
     *
     * @param request données de stock validées
     * @return entrée de stock mise à jour
     */
    @PutMapping
    public InventoryResponse upsert(@Valid @RequestBody InventoryRequest request) {
        return inventoryService.upsert(request);
    }
}

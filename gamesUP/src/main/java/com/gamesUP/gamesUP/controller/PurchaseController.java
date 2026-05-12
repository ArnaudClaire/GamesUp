package com.gamesUP.gamesUP.controller;

import com.gamesUP.gamesUP.dto.PurchaseRequest;
import com.gamesUP.gamesUP.dto.PurchaseResponse;
import com.gamesUP.gamesUP.service.PurchaseService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/purchases")
/**
 * Expose les endpoints REST liés aux commandes et à leur cycle de livraison.
 */
public class PurchaseController {

    private final PurchaseService purchaseService;

    /**
     * Crée le contrôleur avec sa dépendance vers le service des commandes.
     *
     * @param purchaseService service responsable des opérations de commande
     */
    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    /**
     * Liste toutes les commandes ou les filtre par utilisateur.
     *
     * @param userId identifiant utilisateur optionnel
     * @return commandes correspondantes
     */
    @GetMapping
    public List<PurchaseResponse> findAll(@RequestParam(required = false) Long userId) {
        return userId == null ? purchaseService.findAll() : purchaseService.findByUser(userId);
    }

    /**
     * Recherche une commande par son identifiant.
     *
     * @param id identifiant de la commande
     * @return commande demandée
     */
    @GetMapping("/{id}")
    public PurchaseResponse findById(@PathVariable Long id) {
        return purchaseService.findById(id);
    }

    /**
     * Crée une nouvelle commande.
     *
     * @param request données de commande validées
     * @return réponse de la commande créée avec son emplacement
     */
    @PostMapping
    public ResponseEntity<PurchaseResponse> create(@Valid @RequestBody PurchaseRequest request) {
        PurchaseResponse response = purchaseService.create(request);
        return ResponseEntity.created(URI.create("/api/purchases/" + response.id())).body(response);
    }

    /**
     * Marque une commande comme payée.
     *
     * @param id identifiant de la commande
     * @return commande mise à jour
     */
    @PatchMapping("/{id}/paid")
    public PurchaseResponse markPaid(@PathVariable Long id) {
        return purchaseService.markPaid(id);
    }

    /**
     * Marque une commande comme livrée.
     *
     * @param id identifiant de la commande
     * @return commande mise à jour
     */
    @PatchMapping("/{id}/delivered")
    public PurchaseResponse markDelivered(@PathVariable Long id) {
        return purchaseService.markDelivered(id);
    }

    /**
     * Archive une commande.
     *
     * @param id identifiant de la commande
     * @return commande mise à jour
     */
    @PatchMapping("/{id}/archive")
    public PurchaseResponse archive(@PathVariable Long id) {
        return purchaseService.archive(id);
    }
}

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
 * Exposes REST endpoints for purchases and their delivery workflow.
 */
public class PurchaseController {

    private final PurchaseService purchaseService;

    /**
     * Creates the controller with the purchase service dependency.
     *
     * @param purchaseService service responsible for purchase operations
     */
    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    /**
     * Lists every purchase or filters purchases by user.
     *
     * @param userId optional user identifier
     * @return matching purchases
     */
    @GetMapping
    public List<PurchaseResponse> findAll(@RequestParam(required = false) Long userId) {
        return userId == null ? purchaseService.findAll() : purchaseService.findByUser(userId);
    }

    /**
     * Finds a purchase by identifier.
     *
     * @param id purchase identifier
     * @return requested purchase
     */
    @GetMapping("/{id}")
    public PurchaseResponse findById(@PathVariable Long id) {
        return purchaseService.findById(id);
    }

    /**
     * Creates a new purchase.
     *
     * @param request validated purchase payload
     * @return created purchase response with its location
     */
    @PostMapping
    public ResponseEntity<PurchaseResponse> create(@Valid @RequestBody PurchaseRequest request) {
        PurchaseResponse response = purchaseService.create(request);
        return ResponseEntity.created(URI.create("/api/purchases/" + response.id())).body(response);
    }

    /**
     * Marks a purchase as paid.
     *
     * @param id purchase identifier
     * @return updated purchase
     */
    @PatchMapping("/{id}/paid")
    public PurchaseResponse markPaid(@PathVariable Long id) {
        return purchaseService.markPaid(id);
    }

    /**
     * Marks a purchase as delivered.
     *
     * @param id purchase identifier
     * @return updated purchase
     */
    @PatchMapping("/{id}/delivered")
    public PurchaseResponse markDelivered(@PathVariable Long id) {
        return purchaseService.markDelivered(id);
    }

    /**
     * Archives a purchase.
     *
     * @param id purchase identifier
     * @return updated purchase
     */
    @PatchMapping("/{id}/archive")
    public PurchaseResponse archive(@PathVariable Long id) {
        return purchaseService.archive(id);
    }
}

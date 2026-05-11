package com.gamesUP.gamesUP.controller;

import com.gamesUP.gamesUP.dto.PurchaseRequest;
import com.gamesUP.gamesUP.dto.PurchaseResponse;
import com.gamesUP.gamesUP.service.PurchaseService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    @GetMapping
    public List<PurchaseResponse> findAll(@RequestParam(required = false) Long userId) {
        return userId == null ? purchaseService.findAll() : purchaseService.findByUser(userId);
    }

    @GetMapping("/{id}")
    public PurchaseResponse findById(@PathVariable Long id) {
        return purchaseService.findById(id);
    }

    @PostMapping
    public ResponseEntity<PurchaseResponse> create(@Valid @RequestBody PurchaseRequest request) {
        PurchaseResponse response = purchaseService.create(request);
        return ResponseEntity.created(URI.create("/api/purchases/" + response.id())).body(response);
    }

    @PatchMapping("/{id}/paid")
    public PurchaseResponse markPaid(@PathVariable Long id) {
        return purchaseService.markPaid(id);
    }

    @PatchMapping("/{id}/delivered")
    public PurchaseResponse markDelivered(@PathVariable Long id) {
        return purchaseService.markDelivered(id);
    }

    @PatchMapping("/{id}/archive")
    public PurchaseResponse archive(@PathVariable Long id) {
        return purchaseService.archive(id);
    }
}

package com.gamesUP.gamesUP.controller;

import com.gamesUP.gamesUP.dto.ReferenceRequest;
import com.gamesUP.gamesUP.dto.ReferenceResponse;
import com.gamesUP.gamesUP.service.ReferenceService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReferenceController {

    private final ReferenceService referenceService;

    @GetMapping("/api/categories")
    public List<ReferenceResponse> findCategories() {
        return referenceService.findCategories();
    }

    @PostMapping("/api/categories")
    public ResponseEntity<ReferenceResponse> createCategory(@Valid @RequestBody ReferenceRequest request) {
        ReferenceResponse response = referenceService.createCategory(request);
        return ResponseEntity.created(URI.create("/api/categories/" + response.id())).body(response);
    }

    @PutMapping("/api/categories/{id}")
    public ReferenceResponse updateCategory(@PathVariable Long id, @Valid @RequestBody ReferenceRequest request) {
        return referenceService.updateCategory(id, request);
    }

    @DeleteMapping("/api/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        referenceService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/authors")
    public List<ReferenceResponse> findAuthors() {
        return referenceService.findAuthors();
    }

    @PostMapping("/api/authors")
    public ResponseEntity<ReferenceResponse> createAuthor(@Valid @RequestBody ReferenceRequest request) {
        ReferenceResponse response = referenceService.createAuthor(request);
        return ResponseEntity.created(URI.create("/api/authors/" + response.id())).body(response);
    }

    @PutMapping("/api/authors/{id}")
    public ReferenceResponse updateAuthor(@PathVariable Long id, @Valid @RequestBody ReferenceRequest request) {
        return referenceService.updateAuthor(id, request);
    }

    @DeleteMapping("/api/authors/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        referenceService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/publishers")
    public List<ReferenceResponse> findPublishers() {
        return referenceService.findPublishers();
    }

    @PostMapping("/api/publishers")
    public ResponseEntity<ReferenceResponse> createPublisher(@Valid @RequestBody ReferenceRequest request) {
        ReferenceResponse response = referenceService.createPublisher(request);
        return ResponseEntity.created(URI.create("/api/publishers/" + response.id())).body(response);
    }

    @PutMapping("/api/publishers/{id}")
    public ReferenceResponse updatePublisher(@PathVariable Long id, @Valid @RequestBody ReferenceRequest request) {
        return referenceService.updatePublisher(id, request);
    }

    @DeleteMapping("/api/publishers/{id}")
    public ResponseEntity<Void> deletePublisher(@PathVariable Long id) {
        referenceService.deletePublisher(id);
        return ResponseEntity.noContent().build();
    }
}

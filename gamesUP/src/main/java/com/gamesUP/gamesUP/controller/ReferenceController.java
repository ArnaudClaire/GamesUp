package com.gamesUP.gamesUP.controller;

import com.gamesUP.gamesUP.dto.ReferenceRequest;
import com.gamesUP.gamesUP.dto.ReferenceResponse;
import com.gamesUP.gamesUP.service.ReferenceService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
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
/**
 * Exposes REST endpoints for category, author and publisher reference data.
 */
public class ReferenceController {

    private final ReferenceService referenceService;

    /**
     * Creates the controller with the reference service dependency.
     *
     * @param referenceService service responsible for reference data
     */
    public ReferenceController(ReferenceService referenceService) {
        this.referenceService = referenceService;
    }

    /**
     * Lists all game categories.
     *
     * @return categories
     */
    @GetMapping("/api/categories")
    public List<ReferenceResponse> findCategories() {
        return referenceService.findCategories();
    }

    /**
     * Creates a category.
     *
     * @param request validated reference payload
     * @return created category
     */
    @PostMapping("/api/categories")
    public ResponseEntity<ReferenceResponse> createCategory(@Valid @RequestBody ReferenceRequest request) {
        ReferenceResponse response = referenceService.createCategory(request);
        return ResponseEntity.created(URI.create("/api/categories/" + response.id())).body(response);
    }

    /**
     * Updates a category.
     *
     * @param id category identifier
     * @param request validated reference payload
     * @return updated category
     */
    @PutMapping("/api/categories/{id}")
    public ReferenceResponse updateCategory(@PathVariable Long id, @Valid @RequestBody ReferenceRequest request) {
        return referenceService.updateCategory(id, request);
    }

    /**
     * Deletes a category.
     *
     * @param id category identifier
     * @return empty response when the deletion succeeds
     */
    @DeleteMapping("/api/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        referenceService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Lists all authors.
     *
     * @return authors
     */
    @GetMapping("/api/authors")
    public List<ReferenceResponse> findAuthors() {
        return referenceService.findAuthors();
    }

    /**
     * Creates an author.
     *
     * @param request validated reference payload
     * @return created author
     */
    @PostMapping("/api/authors")
    public ResponseEntity<ReferenceResponse> createAuthor(@Valid @RequestBody ReferenceRequest request) {
        ReferenceResponse response = referenceService.createAuthor(request);
        return ResponseEntity.created(URI.create("/api/authors/" + response.id())).body(response);
    }

    /**
     * Updates an author.
     *
     * @param id author identifier
     * @param request validated reference payload
     * @return updated author
     */
    @PutMapping("/api/authors/{id}")
    public ReferenceResponse updateAuthor(@PathVariable Long id, @Valid @RequestBody ReferenceRequest request) {
        return referenceService.updateAuthor(id, request);
    }

    /**
     * Deletes an author.
     *
     * @param id author identifier
     * @return empty response when the deletion succeeds
     */
    @DeleteMapping("/api/authors/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        referenceService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Lists all publishers.
     *
     * @return publishers
     */
    @GetMapping("/api/publishers")
    public List<ReferenceResponse> findPublishers() {
        return referenceService.findPublishers();
    }

    /**
     * Creates a publisher.
     *
     * @param request validated reference payload
     * @return created publisher
     */
    @PostMapping("/api/publishers")
    public ResponseEntity<ReferenceResponse> createPublisher(@Valid @RequestBody ReferenceRequest request) {
        ReferenceResponse response = referenceService.createPublisher(request);
        return ResponseEntity.created(URI.create("/api/publishers/" + response.id())).body(response);
    }

    /**
     * Updates a publisher.
     *
     * @param id publisher identifier
     * @param request validated reference payload
     * @return updated publisher
     */
    @PutMapping("/api/publishers/{id}")
    public ReferenceResponse updatePublisher(@PathVariable Long id, @Valid @RequestBody ReferenceRequest request) {
        return referenceService.updatePublisher(id, request);
    }

    /**
     * Deletes a publisher.
     *
     * @param id publisher identifier
     * @return empty response when the deletion succeeds
     */
    @DeleteMapping("/api/publishers/{id}")
    public ResponseEntity<Void> deletePublisher(@PathVariable Long id) {
        referenceService.deletePublisher(id);
        return ResponseEntity.noContent().build();
    }
}

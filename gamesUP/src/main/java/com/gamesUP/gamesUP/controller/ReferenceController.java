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
 * Expose les endpoints REST liés aux catégories, auteurs et éditeurs.
 */
public class ReferenceController {

    private final ReferenceService referenceService;

    /**
     * Crée le contrôleur avec sa dépendance vers le service des données de référence.
     *
     * @param referenceService service responsable des données de référence
     */
    public ReferenceController(ReferenceService referenceService) {
        this.referenceService = referenceService;
    }

    /**
     * Liste toutes les catégories de jeux.
     *
     * @return catégories
     */
    @GetMapping("/api/categories")
    public List<ReferenceResponse> findCategories() {
        return referenceService.findCategories();
    }

    /**
     * Crée une catégorie.
     *
     * @param request données de référence validées
     * @return catégorie créée
     */
    @PostMapping("/api/categories")
    public ResponseEntity<ReferenceResponse> createCategory(@Valid @RequestBody ReferenceRequest request) {
        ReferenceResponse response = referenceService.createCategory(request);
        return ResponseEntity.created(URI.create("/api/categories/" + response.id())).body(response);
    }

    /**
     * Met à jour une catégorie.
     *
     * @param id identifiant de la catégorie
     * @param request données de référence validées
     * @return catégorie mise à jour
     */
    @PutMapping("/api/categories/{id}")
    public ReferenceResponse updateCategory(@PathVariable Long id, @Valid @RequestBody ReferenceRequest request) {
        return referenceService.updateCategory(id, request);
    }

    /**
     * Supprime une catégorie.
     *
     * @param id identifiant de la catégorie
     * @return réponse vide lorsque la suppression réussit
     */
    @DeleteMapping("/api/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        referenceService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Liste tous les auteurs.
     *
     * @return auteurs
     */
    @GetMapping("/api/authors")
    public List<ReferenceResponse> findAuthors() {
        return referenceService.findAuthors();
    }

    /**
     * Crée un auteur.
     *
     * @param request données de référence validées
     * @return auteur créé
     */
    @PostMapping("/api/authors")
    public ResponseEntity<ReferenceResponse> createAuthor(@Valid @RequestBody ReferenceRequest request) {
        ReferenceResponse response = referenceService.createAuthor(request);
        return ResponseEntity.created(URI.create("/api/authors/" + response.id())).body(response);
    }

    /**
     * Met à jour un auteur.
     *
     * @param id identifiant de l'auteur
     * @param request données de référence validées
     * @return auteur mis à jour
     */
    @PutMapping("/api/authors/{id}")
    public ReferenceResponse updateAuthor(@PathVariable Long id, @Valid @RequestBody ReferenceRequest request) {
        return referenceService.updateAuthor(id, request);
    }

    /**
     * Supprime un auteur.
     *
     * @param id identifiant de l'auteur
     * @return réponse vide lorsque la suppression réussit
     */
    @DeleteMapping("/api/authors/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        referenceService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Liste tous les éditeurs.
     *
     * @return éditeurs
     */
    @GetMapping("/api/publishers")
    public List<ReferenceResponse> findPublishers() {
        return referenceService.findPublishers();
    }

    /**
     * Crée un éditeur.
     *
     * @param request données de référence validées
     * @return éditeur créé
     */
    @PostMapping("/api/publishers")
    public ResponseEntity<ReferenceResponse> createPublisher(@Valid @RequestBody ReferenceRequest request) {
        ReferenceResponse response = referenceService.createPublisher(request);
        return ResponseEntity.created(URI.create("/api/publishers/" + response.id())).body(response);
    }

    /**
     * Met à jour un éditeur.
     *
     * @param id identifiant de l'éditeur
     * @param request données de référence validées
     * @return éditeur mis à jour
     */
    @PutMapping("/api/publishers/{id}")
    public ReferenceResponse updatePublisher(@PathVariable Long id, @Valid @RequestBody ReferenceRequest request) {
        return referenceService.updatePublisher(id, request);
    }

    /**
     * Supprime un éditeur.
     *
     * @param id identifiant de l'éditeur
     * @return réponse vide lorsque la suppression réussit
     */
    @DeleteMapping("/api/publishers/{id}")
    public ResponseEntity<Void> deletePublisher(@PathVariable Long id) {
        referenceService.deletePublisher(id);
        return ResponseEntity.noContent().build();
    }
}

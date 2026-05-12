package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.dto.ReferenceRequest;
import com.gamesUP.gamesUP.dto.ReferenceResponse;
import com.gamesUP.gamesUP.exception.ResourceNotFoundException;
import com.gamesUP.gamesUP.model.Author;
import com.gamesUP.gamesUP.model.Category;
import com.gamesUP.gamesUP.model.Publisher;
import com.gamesUP.gamesUP.repository.AuthorRepository;
import com.gamesUP.gamesUP.repository.CategoryRepository;
import com.gamesUP.gamesUP.repository.PublisherRepository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
/**
 * Fournit les opérations métier liées aux catégories, auteurs et éditeurs.
 */
public class ReferenceService {

    private final CategoryRepository categoryRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;

    /**
     * Crée le service avec ses dépendances vers les dépôts.
     *
     * @param categoryRepository dépôt des catégories
     * @param authorRepository dépôt des auteurs
     * @param publisherRepository dépôt des éditeurs
     */
    public ReferenceService(
            CategoryRepository categoryRepository,
            AuthorRepository authorRepository,
            PublisherRepository publisherRepository
    ) {
        this.categoryRepository = categoryRepository;
        this.authorRepository = authorRepository;
        this.publisherRepository = publisherRepository;
    }

    /**
     * Liste toutes les catégories.
     *
     * @return catégories
     */
    public List<ReferenceResponse> findCategories() {
        return categoryRepository.findAll().stream().map(this::toResponse).toList();
    }

    /**
     * Crée une catégorie.
     *
     * @param request données de référence
     * @return catégorie créée
     */
    @Transactional
    public ReferenceResponse createCategory(ReferenceRequest request) {
        Category category = new Category();
        category.setName(request.name());
        return toResponse(categoryRepository.save(category));
    }

    /**
     * Met à jour une catégorie.
     *
     * @param id identifiant de la catégorie
     * @param request données de référence
     * @return catégorie mise à jour
     */
    @Transactional
    public ReferenceResponse updateCategory(Long id, ReferenceRequest request) {
        Category category = findById(categoryRepository, id, "Category");
        category.setName(request.name());
        return toResponse(category);
    }

    /**
     * Supprime une catégorie.
     *
     * @param id identifiant de la catégorie
     */
    @Transactional
    public void deleteCategory(Long id) {
        categoryRepository.delete(findById(categoryRepository, id, "Category"));
    }

    /**
     * Liste tous les auteurs.
     *
     * @return auteurs
     */
    public List<ReferenceResponse> findAuthors() {
        return authorRepository.findAll().stream().map(this::toResponse).toList();
    }

    /**
     * Crée un auteur.
     *
     * @param request données de référence
     * @return auteur créé
     */
    @Transactional
    public ReferenceResponse createAuthor(ReferenceRequest request) {
        Author author = new Author();
        author.setName(request.name());
        return toResponse(authorRepository.save(author));
    }

    /**
     * Met à jour un auteur.
     *
     * @param id identifiant de l'auteur
     * @param request données de référence
     * @return auteur mis à jour
     */
    @Transactional
    public ReferenceResponse updateAuthor(Long id, ReferenceRequest request) {
        Author author = findById(authorRepository, id, "Author");
        author.setName(request.name());
        return toResponse(author);
    }

    /**
     * Supprime un auteur.
     *
     * @param id identifiant de l'auteur
     */
    @Transactional
    public void deleteAuthor(Long id) {
        authorRepository.delete(findById(authorRepository, id, "Author"));
    }

    /**
     * Liste tous les éditeurs.
     *
     * @return éditeurs
     */
    public List<ReferenceResponse> findPublishers() {
        return publisherRepository.findAll().stream().map(this::toResponse).toList();
    }

    /**
     * Crée un éditeur.
     *
     * @param request données de référence
     * @return éditeur créé
     */
    @Transactional
    public ReferenceResponse createPublisher(ReferenceRequest request) {
        Publisher publisher = new Publisher();
        publisher.setName(request.name());
        return toResponse(publisherRepository.save(publisher));
    }

    /**
     * Met à jour un éditeur.
     *
     * @param id identifiant de l'éditeur
     * @param request données de référence
     * @return éditeur mis à jour
     */
    @Transactional
    public ReferenceResponse updatePublisher(Long id, ReferenceRequest request) {
        Publisher publisher = findById(publisherRepository, id, "Publisher");
        publisher.setName(request.name());
        return toResponse(publisher);
    }

    /**
     * Supprime un éditeur.
     *
     * @param id identifiant de l'éditeur
     */
    @Transactional
    public void deletePublisher(Long id) {
        publisherRepository.delete(findById(publisherRepository, id, "Publisher"));
    }

    private <T> T findById(JpaRepository<T, Long> repository, Long id, String label) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(label + " not found with id " + id));
    }

    private ReferenceResponse toResponse(Category category) {
        return new ReferenceResponse(category.getId(), category.getName());
    }

    private ReferenceResponse toResponse(Author author) {
        return new ReferenceResponse(author.getId(), author.getName());
    }

    private ReferenceResponse toResponse(Publisher publisher) {
        return new ReferenceResponse(publisher.getId(), publisher.getName());
    }
}

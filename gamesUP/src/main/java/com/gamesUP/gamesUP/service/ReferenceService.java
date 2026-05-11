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
 * Provides business operations for categories, authors and publishers.
 */
public class ReferenceService {

    private final CategoryRepository categoryRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;

    /**
     * Creates the service with its repository dependencies.
     *
     * @param categoryRepository repository for categories
     * @param authorRepository repository for authors
     * @param publisherRepository repository for publishers
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
     * Lists all categories.
     *
     * @return categories
     */
    public List<ReferenceResponse> findCategories() {
        return categoryRepository.findAll().stream().map(this::toResponse).toList();
    }

    /**
     * Creates a category.
     *
     * @param request reference payload
     * @return created category
     */
    @Transactional
    public ReferenceResponse createCategory(ReferenceRequest request) {
        Category category = new Category();
        category.setName(request.name());
        return toResponse(categoryRepository.save(category));
    }

    /**
     * Updates a category.
     *
     * @param id category identifier
     * @param request reference payload
     * @return updated category
     */
    @Transactional
    public ReferenceResponse updateCategory(Long id, ReferenceRequest request) {
        Category category = findById(categoryRepository, id, "Category");
        category.setName(request.name());
        return toResponse(category);
    }

    /**
     * Deletes a category.
     *
     * @param id category identifier
     */
    @Transactional
    public void deleteCategory(Long id) {
        categoryRepository.delete(findById(categoryRepository, id, "Category"));
    }

    /**
     * Lists all authors.
     *
     * @return authors
     */
    public List<ReferenceResponse> findAuthors() {
        return authorRepository.findAll().stream().map(this::toResponse).toList();
    }

    /**
     * Creates an author.
     *
     * @param request reference payload
     * @return created author
     */
    @Transactional
    public ReferenceResponse createAuthor(ReferenceRequest request) {
        Author author = new Author();
        author.setName(request.name());
        return toResponse(authorRepository.save(author));
    }

    /**
     * Updates an author.
     *
     * @param id author identifier
     * @param request reference payload
     * @return updated author
     */
    @Transactional
    public ReferenceResponse updateAuthor(Long id, ReferenceRequest request) {
        Author author = findById(authorRepository, id, "Author");
        author.setName(request.name());
        return toResponse(author);
    }

    /**
     * Deletes an author.
     *
     * @param id author identifier
     */
    @Transactional
    public void deleteAuthor(Long id) {
        authorRepository.delete(findById(authorRepository, id, "Author"));
    }

    /**
     * Lists all publishers.
     *
     * @return publishers
     */
    public List<ReferenceResponse> findPublishers() {
        return publisherRepository.findAll().stream().map(this::toResponse).toList();
    }

    /**
     * Creates a publisher.
     *
     * @param request reference payload
     * @return created publisher
     */
    @Transactional
    public ReferenceResponse createPublisher(ReferenceRequest request) {
        Publisher publisher = new Publisher();
        publisher.setName(request.name());
        return toResponse(publisherRepository.save(publisher));
    }

    /**
     * Updates a publisher.
     *
     * @param id publisher identifier
     * @param request reference payload
     * @return updated publisher
     */
    @Transactional
    public ReferenceResponse updatePublisher(Long id, ReferenceRequest request) {
        Publisher publisher = findById(publisherRepository, id, "Publisher");
        publisher.setName(request.name());
        return toResponse(publisher);
    }

    /**
     * Deletes a publisher.
     *
     * @param id publisher identifier
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

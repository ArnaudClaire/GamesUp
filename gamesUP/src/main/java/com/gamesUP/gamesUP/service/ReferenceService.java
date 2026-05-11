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
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReferenceService {

    private final CategoryRepository categoryRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;

    public List<ReferenceResponse> findCategories() {
        return categoryRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional
    public ReferenceResponse createCategory(ReferenceRequest request) {
        Category category = new Category();
        category.setName(request.name());
        return toResponse(categoryRepository.save(category));
    }

    @Transactional
    public ReferenceResponse updateCategory(Long id, ReferenceRequest request) {
        Category category = findById(categoryRepository, id, "Category");
        category.setName(request.name());
        return toResponse(category);
    }

    @Transactional
    public void deleteCategory(Long id) {
        categoryRepository.delete(findById(categoryRepository, id, "Category"));
    }

    public List<ReferenceResponse> findAuthors() {
        return authorRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional
    public ReferenceResponse createAuthor(ReferenceRequest request) {
        Author author = new Author();
        author.setName(request.name());
        return toResponse(authorRepository.save(author));
    }

    @Transactional
    public ReferenceResponse updateAuthor(Long id, ReferenceRequest request) {
        Author author = findById(authorRepository, id, "Author");
        author.setName(request.name());
        return toResponse(author);
    }

    @Transactional
    public void deleteAuthor(Long id) {
        authorRepository.delete(findById(authorRepository, id, "Author"));
    }

    public List<ReferenceResponse> findPublishers() {
        return publisherRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional
    public ReferenceResponse createPublisher(ReferenceRequest request) {
        Publisher publisher = new Publisher();
        publisher.setName(request.name());
        return toResponse(publisherRepository.save(publisher));
    }

    @Transactional
    public ReferenceResponse updatePublisher(Long id, ReferenceRequest request) {
        Publisher publisher = findById(publisherRepository, id, "Publisher");
        publisher.setName(request.name());
        return toResponse(publisher);
    }

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

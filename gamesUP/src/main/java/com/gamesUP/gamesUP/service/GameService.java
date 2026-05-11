package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.dto.GameRequest;
import com.gamesUP.gamesUP.dto.GameResponse;
import com.gamesUP.gamesUP.exception.ResourceNotFoundException;
import com.gamesUP.gamesUP.mapper.GameMapper;
import com.gamesUP.gamesUP.model.Author;
import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.repository.AuthorRepository;
import com.gamesUP.gamesUP.repository.CategoryRepository;
import com.gamesUP.gamesUP.repository.GameRepository;
import com.gamesUP.gamesUP.repository.PublisherRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional(readOnly = true)
/**
 * Provides business operations for the game catalogue.
 */
public class GameService {

    private final GameRepository gameRepository;
    private final CategoryRepository categoryRepository;
    private final PublisherRepository publisherRepository;
    private final AuthorRepository authorRepository;
    private final GameMapper gameMapper;

    /**
     * Creates the service with its persistence and mapping dependencies.
     *
     * @param gameRepository repository for games
     * @param categoryRepository repository for categories
     * @param publisherRepository repository for publishers
     * @param authorRepository repository for authors
     * @param gameMapper mapper used to convert games to API DTOs
     */
    public GameService(
            GameRepository gameRepository,
            CategoryRepository categoryRepository,
            PublisherRepository publisherRepository,
            AuthorRepository authorRepository,
            GameMapper gameMapper
    ) {
        this.gameRepository = gameRepository;
        this.categoryRepository = categoryRepository;
        this.publisherRepository = publisherRepository;
        this.authorRepository = authorRepository;
        this.gameMapper = gameMapper;
    }

    /**
     * Lists every game or applies a free-text search.
     *
     * @param search optional search term
     * @return matching games
     */
    public List<GameResponse> findAll(String search) {
        List<Game> games = StringUtils.hasText(search)
                ? gameRepository.search(search.trim())
                : gameRepository.findAll();

        return games.stream()
                .map(gameMapper::toResponse)
                .toList();
    }

    /**
     * Finds one game by identifier.
     *
     * @param id game identifier
     * @return requested game
     */
    public GameResponse findById(Long id) {
        return gameMapper.toResponse(findGame(id));
    }

    /**
     * Creates a game from the public request DTO.
     *
     * @param request game payload
     * @return created game
     */
    @Transactional
    public GameResponse create(GameRequest request) {
        Game game = new Game();
        applyRequest(game, request);
        return gameMapper.toResponse(gameRepository.save(game));
    }

    /**
     * Updates an existing game from the public request DTO.
     *
     * @param id game identifier
     * @param request game payload
     * @return updated game
     */
    @Transactional
    public GameResponse update(Long id, GameRequest request) {
        Game game = findGame(id);
        applyRequest(game, request);
        return gameMapper.toResponse(game);
    }

    /**
     * Deletes an existing game.
     *
     * @param id game identifier
     */
    @Transactional
    public void delete(Long id) {
        Game game = findGame(id);
        gameRepository.delete(game);
    }

    private Game findGame(Long id) {
        return gameRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Game not found with id " + id));
    }

    private void applyRequest(Game game, GameRequest request) {
        gameMapper.updateEntity(game, request);
        game.setCategory(resolveCategory(request.categoryId()));
        game.setPublisher(resolvePublisher(request.publisherId()));
        game.setAuthors(resolveAuthors(request.authorIds()));
    }

    private com.gamesUP.gamesUP.model.Category resolveCategory(Long id) {
        if (id == null) {
            return null;
        }
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + id));
    }

    private com.gamesUP.gamesUP.model.Publisher resolvePublisher(Long id) {
        if (id == null) {
            return null;
        }
        return publisherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher not found with id " + id));
    }

    private Set<Author> resolveAuthors(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return new HashSet<>();
        }

        Set<Author> authors = new HashSet<>(authorRepository.findAllById(ids));
        if (authors.size() != ids.size()) {
            throw new ResourceNotFoundException("One or more authors were not found");
        }
        return authors;
    }
}

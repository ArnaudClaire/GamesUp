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
 * Fournit les opérations métier du catalogue de jeux.
 */
public class GameService {

    private final GameRepository gameRepository;
    private final CategoryRepository categoryRepository;
    private final PublisherRepository publisherRepository;
    private final AuthorRepository authorRepository;
    private final GameMapper gameMapper;

    /**
     * Crée le service avec ses dépendances de persistance et de mapping.
     *
     * @param gameRepository dépôt des jeux
     * @param categoryRepository dépôt des catégories
     * @param publisherRepository dépôt des éditeurs
     * @param authorRepository dépôt des auteurs
     * @param gameMapper mapper utilisé pour convertir les jeux en DTO d'API
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
     * Liste tous les jeux ou applique une recherche en texte libre.
     *
     * @param search terme de recherche optionnel
     * @return jeux correspondants
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
     * Recherche un jeu par son identifiant.
     *
     * @param id identifiant du jeu
     * @return jeu demandé
     */
    public GameResponse findById(Long id) {
        return gameMapper.toResponse(findGame(id));
    }

    /**
     * Crée un jeu à partir du DTO de requête publique.
     *
     * @param request données du jeu
     * @return jeu créé
     */
    @Transactional
    public GameResponse create(GameRequest request) {
        Game game = new Game();
        applyRequest(game, request);
        return gameMapper.toResponse(gameRepository.save(game));
    }

    /**
     * Met à jour un jeu existant à partir du DTO de requête publique.
     *
     * @param id identifiant du jeu
     * @param request données du jeu
     * @return jeu mis à jour
     */
    @Transactional
    public GameResponse update(Long id, GameRequest request) {
        Game game = findGame(id);
        applyRequest(game, request);
        return gameMapper.toResponse(game);
    }

    /**
     * Supprime un jeu existant.
     *
     * @param id identifiant du jeu
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

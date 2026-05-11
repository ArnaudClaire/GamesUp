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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GameService {

    private final GameRepository gameRepository;
    private final CategoryRepository categoryRepository;
    private final PublisherRepository publisherRepository;
    private final AuthorRepository authorRepository;
    private final GameMapper gameMapper;

    public List<GameResponse> findAll(String search) {
        List<Game> games = StringUtils.hasText(search)
                ? gameRepository.search(search.trim())
                : gameRepository.findAll();

        return games.stream()
                .map(gameMapper::toResponse)
                .toList();
    }

    public GameResponse findById(Long id) {
        return gameMapper.toResponse(findGame(id));
    }

    @Transactional
    public GameResponse create(GameRequest request) {
        Game game = new Game();
        applyRequest(game, request);
        return gameMapper.toResponse(gameRepository.save(game));
    }

    @Transactional
    public GameResponse update(Long id, GameRequest request) {
        Game game = findGame(id);
        applyRequest(game, request);
        return gameMapper.toResponse(game);
    }

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

package com.gamesUP.gamesUP.controller;

import com.gamesUP.gamesUP.dto.GameRequest;
import com.gamesUP.gamesUP.dto.GameResponse;
import com.gamesUP.gamesUP.service.GameService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/games")
/**
 * Exposes REST endpoints used to read, search and administer board games.
 */
public class GameController {

    private final GameService gameService;

    /**
     * Creates the controller with the game service dependency.
     *
     * @param gameService service responsible for game catalogue operations
     */
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * Lists all games or filters them with a free-text search.
     *
     * @param search optional search term
     * @return matching games
     */
    @GetMapping
    public List<GameResponse> findAll(@RequestParam(required = false) String search) {
        return gameService.findAll(search);
    }

    /**
     * Finds one game by its identifier.
     *
     * @param id game identifier
     * @return the requested game
     */
    @GetMapping("/{id}")
    public GameResponse findById(@PathVariable Long id) {
        return gameService.findById(id);
    }

    /**
     * Creates a new game in the catalogue.
     *
     * @param request validated game payload
     * @return created game response with its location
     */
    @PostMapping
    public ResponseEntity<GameResponse> create(@Valid @RequestBody GameRequest request) {
        GameResponse response = gameService.create(request);
        return ResponseEntity
                .created(URI.create("/api/games/" + response.id()))
                .body(response);
    }

    /**
     * Updates an existing game.
     *
     * @param id game identifier
     * @param request validated game payload
     * @return updated game response
     */
    @PutMapping("/{id}")
    public GameResponse update(@PathVariable Long id, @Valid @RequestBody GameRequest request) {
        return gameService.update(id, request);
    }

    /**
     * Deletes a game from the catalogue.
     *
     * @param id game identifier
     * @return empty response when the deletion succeeds
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        gameService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

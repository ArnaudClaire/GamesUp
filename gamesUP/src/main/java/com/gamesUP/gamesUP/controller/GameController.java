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
 * Expose les endpoints REST utilisés pour consulter, rechercher et administrer les jeux de société.
 */
public class GameController {

    private final GameService gameService;

    /**
     * Crée le contrôleur avec sa dépendance vers le service des jeux.
     *
     * @param gameService service responsable des opérations du catalogue de jeux
     */
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * Liste tous les jeux ou les filtre avec une recherche en texte libre.
     *
     * @param search terme de recherche optionnel
     * @return jeux correspondants
     */
    @GetMapping
    public List<GameResponse> findAll(@RequestParam(required = false) String search) {
        return gameService.findAll(search);
    }

    /**
     * Recherche un jeu par son identifiant.
     *
     * @param id identifiant du jeu
     * @return the jeu demandé
     */
    @GetMapping("/{id}")
    public GameResponse findById(@PathVariable Long id) {
        return gameService.findById(id);
    }

    /**
     * Crée un nouveau jeu dans le catalogue.
     *
     * @param request données de jeu validées
     * @return réponse du jeu créé avec son emplacement
     */
    @PostMapping
    public ResponseEntity<GameResponse> create(@Valid @RequestBody GameRequest request) {
        GameResponse response = gameService.create(request);
        return ResponseEntity
                .created(URI.create("/api/games/" + response.id()))
                .body(response);
    }

    /**
     * Met à jour un jeu existant.
     *
     * @param id identifiant du jeu
     * @param request données de jeu validées
     * @return réponse du jeu mis à jour
     */
    @PutMapping("/{id}")
    public GameResponse update(@PathVariable Long id, @Valid @RequestBody GameRequest request) {
        return gameService.update(id, request);
    }

    /**
     * Supprime un jeu du catalogue.
     *
     * @param id identifiant du jeu
     * @return réponse vide lorsque la suppression réussit
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        gameService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

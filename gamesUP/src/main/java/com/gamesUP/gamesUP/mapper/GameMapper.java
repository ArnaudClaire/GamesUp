package com.gamesUP.gamesUP.mapper;

import com.gamesUP.gamesUP.dto.GameRequest;
import com.gamesUP.gamesUP.dto.GameResponse;
import com.gamesUP.gamesUP.dto.ReferenceResponse;
import com.gamesUP.gamesUP.model.Author;
import com.gamesUP.gamesUP.model.Category;
import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.model.Inventory;
import com.gamesUP.gamesUP.model.Publisher;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
/**
 * Maps game entities to API responses and applies request data to entities.
 */
public class GameMapper {

    /**
     * Converts a game entity to its public response DTO.
     *
     * @param game game entity
     * @return public game response
     */
    public GameResponse toResponse(Game game) {
        return new GameResponse(
                game.getId(),
                game.getName(),
                game.getDescription(),
                game.getGenre(),
                game.getMinPlayers(),
                game.getMaxPlayers(),
                game.getDurationMinutes(),
                game.getEditionNumber(),
                game.getPrice(),
                toReference(game.getCategory()),
                toReference(game.getPublisher()),
                toAuthorReferences(game.getAuthors()),
                toStockQuantity(game.getInventory())
        );
    }

    /**
     * Copies writable request fields to a game entity.
     *
     * @param game target game entity
     * @param request source request DTO
     */
    public void updateEntity(Game game, GameRequest request) {
        game.setName(request.name());
        game.setDescription(request.description());
        game.setGenre(request.genre());
        game.setMinPlayers(request.minPlayers());
        game.setMaxPlayers(request.maxPlayers());
        game.setDurationMinutes(request.durationMinutes());
        game.setEditionNumber(request.editionNumber());
        game.setPrice(request.price());
    }

    private ReferenceResponse toReference(Category category) {
        if (category == null) {
            return null;
        }
        return new ReferenceResponse(category.getId(), category.getName());
    }

    private ReferenceResponse toReference(Publisher publisher) {
        if (publisher == null) {
            return null;
        }
        return new ReferenceResponse(publisher.getId(), publisher.getName());
    }

    private Set<ReferenceResponse> toAuthorReferences(Set<Author> authors) {
        return authors.stream()
                .map(author -> new ReferenceResponse(author.getId(), author.getName()))
                .sorted(Comparator.comparing(ReferenceResponse::name, Comparator.nullsLast(String::compareToIgnoreCase)))
                .collect(Collectors.toCollection(java.util.LinkedHashSet::new));
    }

    private Integer toStockQuantity(Inventory inventory) {
        if (inventory == null) {
            return null;
        }
        return inventory.getQuantity();
    }
}

package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.dto.ReferenceResponse;
import com.gamesUP.gamesUP.dto.WishlistRequest;
import com.gamesUP.gamesUP.dto.WishlistResponse;
import com.gamesUP.gamesUP.exception.ResourceNotFoundException;
import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.model.User;
import com.gamesUP.gamesUP.model.Wishlist;
import com.gamesUP.gamesUP.repository.GameRepository;
import com.gamesUP.gamesUP.repository.UserRepository;
import com.gamesUP.gamesUP.repository.WishlistRepository;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Fournit les opérations métier liées aux listes d'envies des utilisateurs.
 */
@Service
@Transactional(readOnly = true)
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;

    /**
     * Crée le service avec ses dépendances vers les dépôts.
     *
     * @param wishlistRepository dépôt des listes d'envies
     * @param userRepository dépôt des utilisateurs
     * @param gameRepository dépôt des jeux
     */
    public WishlistService(
            WishlistRepository wishlistRepository,
            UserRepository userRepository,
            GameRepository gameRepository
    ) {
        this.wishlistRepository = wishlistRepository;
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
    }

    /**
     * Liste toutes les listes d'envies.
     *
     * @return listes d'envies
     */
    public List<WishlistResponse> findAll() {
        return wishlistRepository.findAll().stream().map(this::toResponse).toList();
    }

    /**
     * Recherche une liste d'envies par son identifiant.
     *
     * @param id identifiant de la liste d'envies
     * @return liste d'envies demandée
     */
    public WishlistResponse findById(Long id) {
        return toResponse(findWishlist(id));
    }

    /**
     * Recherche la liste d'envies possédée par un utilisateur.
     *
     * @param userId identifiant utilisateur
     * @return liste d'envies demandée
     */
    public WishlistResponse findByUser(Long userId) {
        return wishlistRepository.findByUserId(userId)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist not found for user id " + userId));
    }

    /**
     * Crée une liste d'envies.
     *
     * @param request données de liste d'envies
     * @return liste d'envies créée
     */
    @Transactional
    public WishlistResponse create(WishlistRequest request) {
        Wishlist wishlist = new Wishlist();
        applyRequest(wishlist, request);
        return toResponse(wishlistRepository.save(wishlist));
    }

    /**
     * Met à jour une liste d'envies existante.
     *
     * @param id identifiant de la liste d'envies
     * @param request données de liste d'envies
     * @return liste d'envies mise à jour
     */
    @Transactional
    public WishlistResponse update(Long id, WishlistRequest request) {
        Wishlist wishlist = findWishlist(id);
        applyRequest(wishlist, request);
        return toResponse(wishlist);
    }

    /**
     * Supprime une liste d'envies.
     *
     * @param id identifiant de la liste d'envies
     */
    @Transactional
    public void delete(Long id) {
        wishlistRepository.delete(findWishlist(id));
    }

    private Wishlist findWishlist(Long id) {
        return wishlistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist not found with id " + id));
    }

    private void applyRequest(Wishlist wishlist, WishlistRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + request.userId()));
        wishlist.setUser(user);
        wishlist.setGames(resolveGames(request.gameIds()));
    }

    private Set<Game> resolveGames(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return new HashSet<>();
        }
        Set<Game> games = new HashSet<>(gameRepository.findAllById(ids));
        if (games.size() != ids.size()) {
            throw new ResourceNotFoundException("One or more wishlist games were not found");
        }
        return games;
    }

    private WishlistResponse toResponse(Wishlist wishlist) {
        User user = wishlist.getUser();
        Set<ReferenceResponse> games = wishlist.getGames().stream()
                .map(game -> new ReferenceResponse(game.getId(), game.getName()))
                .sorted(Comparator.comparing(ReferenceResponse::name, Comparator.nullsLast(String::compareToIgnoreCase)))
                .collect(Collectors.toCollection(java.util.LinkedHashSet::new));
        return new WishlistResponse(
                wishlist.getId(),
                new ReferenceResponse(user.getId(), user.getEmail()),
                games
        );
    }
}

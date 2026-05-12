package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.dto.PurchaseLineRequest;
import com.gamesUP.gamesUP.dto.PurchaseLineResponse;
import com.gamesUP.gamesUP.dto.PurchaseRequest;
import com.gamesUP.gamesUP.dto.PurchaseResponse;
import com.gamesUP.gamesUP.dto.ReferenceResponse;
import com.gamesUP.gamesUP.exception.ResourceNotFoundException;
import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.model.Purchase;
import com.gamesUP.gamesUP.model.PurchaseLine;
import com.gamesUP.gamesUP.model.User;
import com.gamesUP.gamesUP.repository.GameRepository;
import com.gamesUP.gamesUP.repository.PurchaseRepository;
import com.gamesUP.gamesUP.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
/**
 * Fournit les opérations métier liées aux commandes et à leurs changements de statut.
 */
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;

    /**
     * Crée le service avec ses dépendances vers les dépôts.
     *
     * @param purchaseRepository dépôt des commandes
     * @param userRepository dépôt des utilisateurs
     * @param gameRepository dépôt des jeux
     */
    public PurchaseService(
            PurchaseRepository purchaseRepository,
            UserRepository userRepository,
            GameRepository gameRepository
    ) {
        this.purchaseRepository = purchaseRepository;
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
    }

    /**
     * Liste toutes les commandes.
     *
     * @return commandes
     */
    public List<PurchaseResponse> findAll() {
        return purchaseRepository.findAll().stream().map(this::toResponse).toList();
    }

    /**
     * Liste les commandes rattachées à un utilisateur.
     *
     * @param userId identifiant utilisateur
     * @return commandes correspondantes
     */
    public List<PurchaseResponse> findByUser(Long userId) {
        return purchaseRepository.findByUserId(userId).stream().map(this::toResponse).toList();
    }

    /**
     * Recherche une commande par son identifiant.
     *
     * @param id identifiant de la commande
     * @return commande demandée
     */
    public PurchaseResponse findById(Long id) {
        return toResponse(findPurchase(id));
    }

    /**
     * Crée une commande et ses lignes.
     *
     * @param request données de commande
     * @return commande créée
     */
    @Transactional
    public PurchaseResponse create(PurchaseRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + request.userId()));

        Purchase purchase = new Purchase();
        purchase.setUser(user);
        purchase.setDate(LocalDateTime.now());
        purchase.setPaid(false);
        purchase.setDelivered(false);
        purchase.setArchived(false);

        for (PurchaseLineRequest lineRequest : request.lines()) {
            Game game = gameRepository.findById(lineRequest.gameId())
                    .orElseThrow(() -> new ResourceNotFoundException("Game not found with id " + lineRequest.gameId()));
            PurchaseLine line = new PurchaseLine();
            line.setPurchase(purchase);
            line.setGame(game);
            line.setQuantity(lineRequest.quantity());
            line.setUnitPrice(game.getPrice());
            purchase.getLines().add(line);
        }

        return toResponse(purchaseRepository.save(purchase));
    }

    /**
     * Marque une commande comme payée.
     *
     * @param id identifiant de la commande
     * @return commande mise à jour
     */
    @Transactional
    public PurchaseResponse markPaid(Long id) {
        Purchase purchase = findPurchase(id);
        purchase.setPaid(true);
        return toResponse(purchase);
    }

    /**
     * Marque une commande comme livrée.
     *
     * @param id identifiant de la commande
     * @return commande mise à jour
     */
    @Transactional
    public PurchaseResponse markDelivered(Long id) {
        Purchase purchase = findPurchase(id);
        purchase.setDelivered(true);
        return toResponse(purchase);
    }

    /**
     * Archive une commande.
     *
     * @param id identifiant de la commande
     * @return commande mise à jour
     */
    @Transactional
    public PurchaseResponse archive(Long id) {
        Purchase purchase = findPurchase(id);
        purchase.setArchived(true);
        return toResponse(purchase);
    }

    private Purchase findPurchase(Long id) {
        return purchaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase not found with id " + id));
    }

    private PurchaseResponse toResponse(Purchase purchase) {
        User user = purchase.getUser();
        return new PurchaseResponse(
                purchase.getId(),
                new ReferenceResponse(user.getId(), user.getEmail()),
                purchase.getLines().stream().map(this::toLineResponse).toList(),
                purchase.getDate(),
                purchase.isPaid(),
                purchase.isDelivered(),
                purchase.isArchived()
        );
    }

    private PurchaseLineResponse toLineResponse(PurchaseLine line) {
        Game game = line.getGame();
        return new PurchaseLineResponse(line.getId(), game.getId(), game.getName(), line.getQuantity(), line.getUnitPrice());
    }
}

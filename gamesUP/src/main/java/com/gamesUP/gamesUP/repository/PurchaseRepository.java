package com.gamesUP.gamesUP.repository;

import com.gamesUP.gamesUP.model.Purchase;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data repository for purchases.
 */
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    /**
     * Finds purchases made by one user.
     *
     * @param userId user identifier
     * @return matching purchases
     */
    List<Purchase> findByUserId(Long userId);
}

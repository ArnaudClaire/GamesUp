package com.gamesUP.gamesUP.repository;

import com.gamesUP.gamesUP.model.Wishlist;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data repository for wishlists.
 */
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    /**
     * Finds the wishlist owned by one user.
     *
     * @param userId user identifier
     * @return matching wishlist when present
     */
    Optional<Wishlist> findByUserId(Long userId);
}

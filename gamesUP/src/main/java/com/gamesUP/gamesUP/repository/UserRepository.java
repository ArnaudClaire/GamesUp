package com.gamesUP.gamesUP.repository;

import com.gamesUP.gamesUP.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data repository for user accounts.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by email.
     *
     * @param email user email
     * @return matching user when present
     */
    Optional<User> findByEmail(String email);
}

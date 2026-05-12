package com.gamesUP.gamesUP.repository;

import com.gamesUP.gamesUP.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Dépôt Spring Data pour les comptes utilisateur.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Recherche un utilisateur par email.
     *
     * @param email user email
     * @return utilisateur correspondant, s'il existe
     */
    Optional<User> findByEmail(String email);
}

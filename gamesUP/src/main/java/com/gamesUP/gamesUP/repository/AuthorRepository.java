package com.gamesUP.gamesUP.repository;

import com.gamesUP.gamesUP.model.Author;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data repository for authors.
 */
public interface AuthorRepository extends JpaRepository<Author, Long> {

    Optional<Author> findByNameIgnoreCase(String name);
}

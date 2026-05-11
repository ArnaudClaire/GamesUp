package com.gamesUP.gamesUP.repository;

import com.gamesUP.gamesUP.model.Category;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data repository for categories.
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByNameIgnoreCase(String name);
}

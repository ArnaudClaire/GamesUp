package com.gamesUP.gamesUP.repository;

import com.gamesUP.gamesUP.model.Game;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data repository for games and game search.
 */
public interface GameRepository extends JpaRepository<Game, Long> {

    /**
     * Searches games by title, genre, category, publisher or author.
     *
     * @param keyword free-text keyword
     * @return matching games
     */
    @Query("""
            select distinct g
            from Game g
            left join g.category c
            left join g.publisher p
            left join g.authors a
            where lower(g.name) like lower(concat('%', :keyword, '%'))
               or lower(g.genre) like lower(concat('%', :keyword, '%'))
               or lower(c.name) like lower(concat('%', :keyword, '%'))
               or lower(p.name) like lower(concat('%', :keyword, '%'))
               or lower(a.name) like lower(concat('%', :keyword, '%'))
            """)
    List<Game> search(@Param("keyword") String keyword);
}

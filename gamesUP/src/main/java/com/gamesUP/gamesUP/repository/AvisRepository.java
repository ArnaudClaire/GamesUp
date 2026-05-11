package com.gamesUP.gamesUP.repository;

import com.gamesUP.gamesUP.model.Avis;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvisRepository extends JpaRepository<Avis, Long> {

    List<Avis> findByGameId(Long gameId);

    List<Avis> findByUserId(Long userId);
}

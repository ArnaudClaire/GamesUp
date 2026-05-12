package com.gamesUP.gamesUP.controller;

import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
/**
 * Expose les endpoints publics utilisés pour vérifier que l'API fonctionne.
 */
public class HealthController {

    /**
     * Renvoie un message d'accueil public.
     *
     * @return API status message
     */
    @GetMapping("/")
    public Map<String, String> root() {
        return Map.of("message", "GamesUP API is running");
    }

    /**
     * Renvoie des informations techniques de santé pour les tests de démarrage.
     *
     * @return données d'état de santé
     */
    @GetMapping("/api/health")
    public Map<String, Object> health() {
        return Map.of(
                "status", "UP",
                "application", "gamesUP",
                "timestamp", LocalDateTime.now()
        );
    }
}

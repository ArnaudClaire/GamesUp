package com.gamesUP.gamesUP.controller;

import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
/**
 * Exposes public endpoints used to verify that the API is running.
 */
public class HealthController {

    /**
     * Returns a public welcome message.
     *
     * @return API status message
     */
    @GetMapping("/")
    public Map<String, String> root() {
        return Map.of("message", "GamesUP API is running");
    }

    /**
     * Returns technical health information for smoke tests.
     *
     * @return health status payload
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

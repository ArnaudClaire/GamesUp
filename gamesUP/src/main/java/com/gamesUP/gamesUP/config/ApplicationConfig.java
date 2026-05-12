package com.gamesUP.gamesUP.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
/**
 * Fournit les beans d'infrastructure réutilisables de l'application.
 */
public class ApplicationConfig {

    /**
     * Crée le client HTTP utilisé pour appeler les services externes.
     *
     * @param builder builder RestTemplate de Spring Boot
     * @return RestTemplate configuré
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}

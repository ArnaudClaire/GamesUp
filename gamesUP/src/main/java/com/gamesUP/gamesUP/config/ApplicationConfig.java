package com.gamesUP.gamesUP.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
/**
 * Provides reusable infrastructure beans for the application.
 */
public class ApplicationConfig {

    /**
     * Creates the HTTP client used to call external services.
     *
     * @param builder Spring Boot RestTemplate builder
     * @return configured RestTemplate
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}

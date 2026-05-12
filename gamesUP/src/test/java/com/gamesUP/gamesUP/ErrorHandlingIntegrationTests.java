package com.gamesUP.gamesUP;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

/**
 * Tests d'intégration des réponses d'erreur exposées par l'API.
 */
class ErrorHandlingIntegrationTests extends AbstractApiIntegrationTest {

    /**
     * Vérifie les formats d'erreur pour les validations, absences de ressource et références invalides.
     *
     * @throws Exception si l'appel MockMvc échoue.
     */
    @Test
    void apiReturnsStructuredErrorsForCommonFailures() throws Exception {
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "firstName": "",
                                  "lastName": "",
                                  "email": "not-an-email",
                                  "password": "123"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation failed"));

        mockMvc.perform(get("/api/games/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));

        mockMvc.perform(post("/api/games")
                        .with(user("admin@gamesup.test").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Broken Game",
                                  "description": "Categorie inconnue.",
                                  "genre": "Erreur",
                                  "minPlayers": 1,
                                  "maxPlayers": 2,
                                  "durationMinutes": 10,
                                  "editionNumber": 1,
                                  "price": 1.00,
                                  "categoryId": 99999,
                                  "publisherId": null,
                                  "authorIds": []
                                }
                                """))
                .andExpect(status().isNotFound());
    }
}

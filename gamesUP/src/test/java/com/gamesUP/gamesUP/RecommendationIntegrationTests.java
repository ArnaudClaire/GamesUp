package com.gamesUP.gamesUP;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class RecommendationIntegrationTests extends AbstractApiIntegrationTest {

    @Test
    void recommendationProxyReturnsPythonResponse() throws Exception {
        mockPythonRecommendation();

        mockMvc.perform(post("/api/recommendations")
                        .with(user("client@gamesup.test").roles("CLIENT"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "userId": 1,
                                  "purchases": [
                                    {
                                      "gameId": 102,
                                      "rating": 4.5
                                    }
                                  ]
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].gameName").value("Azul"));
    }

    @Test
    void recommendationEndpointBuildsUserSignalsBeforeCallingPython() throws Exception {
        mockPythonRecommendation();

        long userId = extractId(mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "firstName": "Reco",
                                  "lastName": "Client",
                                  "email": "reco@gamesup.test",
                                  "password": "reco123"
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString());

        long categoryId = extractId(mockMvc.perform(post("/api/categories")
                        .with(user("admin@gamesup.test").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Reco Category\"}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString());

        long gameId = extractId(mockMvc.perform(post("/api/games")
                        .with(user("admin@gamesup.test").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Reco Game",
                                  "description": "Jeu pour recommandations.",
                                  "genre": "Reco",
                                  "minPlayers": 1,
                                  "maxPlayers": 4,
                                  "durationMinutes": 45,
                                  "editionNumber": 1,
                                  "price": 29.99,
                                  "categoryId": %d,
                                  "publisherId": null,
                                  "authorIds": []
                                }
                                """.formatted(categoryId)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString());

        mockMvc.perform(post("/api/purchases")
                        .with(user("admin@gamesup.test").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "userId": %d,
                                  "lines": [
                                    {
                                      "gameId": %d,
                                      "quantity": 1
                                    }
                                  ]
                                }
                                """.formatted(userId, gameId)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/recommendations/users/" + userId)
                        .with(user("admin@gamesup.test").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].gameName").value("Azul"));
    }
}

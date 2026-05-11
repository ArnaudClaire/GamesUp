package com.gamesUP.gamesUP;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class CatalogIntegrationTests extends AbstractApiIntegrationTest {

    @Test
    void adminCanCreateReferenceAndGameThenSearchIt() throws Exception {
        long categoryId = extractId(mockMvc.perform(post("/api/categories")
                        .with(user("admin@gamesup.test").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Expert\"}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString());

        mockMvc.perform(post("/api/games")
                        .with(user("admin@gamesup.test").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Terraforming Mars",
                                  "description": "Developper Mars avec des cartes et ressources.",
                                  "genre": "Strategie",
                                  "minPlayers": 1,
                                  "maxPlayers": 5,
                                  "durationMinutes": 120,
                                  "editionNumber": 1,
                                  "price": 49.99,
                                  "categoryId": %d,
                                  "publisherId": null,
                                  "authorIds": []
                                }
                                """.formatted(categoryId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Terraforming Mars"));

        mockMvc.perform(get("/api/games?search=mars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Terraforming Mars"));
    }

    @Test
    void gameResponseIncludesInventoryWhenStockExists() throws Exception {
        long gameId = extractId(mockMvc.perform(post("/api/games")
                        .with(user("admin@gamesup.test").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Stocked Game",
                                  "description": "Jeu sans categorie avec stock.",
                                  "genre": "Stock",
                                  "minPlayers": 1,
                                  "maxPlayers": 4,
                                  "durationMinutes": 30,
                                  "editionNumber": 1,
                                  "price": 19.99,
                                  "categoryId": null,
                                  "publisherId": null,
                                  "authorIds": []
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.category").doesNotExist())
                .andReturn().getResponse().getContentAsString());

        mockMvc.perform(put("/api/inventory")
                        .with(user("admin@gamesup.test").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "gameId": %d,
                                  "quantity": 5
                                }
                                """.formatted(gameId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(5));

        mockMvc.perform(get("/api/games/" + gameId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stockQuantity").value(5));
    }
}

package com.gamesUP.gamesUP;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class CommerceIntegrationTests extends AbstractApiIntegrationTest {

    @Test
    void adminWorkflowCoversWishlistReviewsInventoryAndPurchases() throws Exception {
        long userId = extractId(mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "firstName": "Workflow",
                                  "lastName": "Client",
                                  "email": "workflow@gamesup.test",
                                  "password": "workflow123"
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString());

        long categoryId = extractId(mockMvc.perform(post("/api/categories")
                        .with(user("admin@gamesup.test").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Cooperatif\"}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString());

        long gameId = extractId(mockMvc.perform(post("/api/games")
                        .with(user("admin@gamesup.test").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Pandemic Legacy",
                                  "description": "Campagne cooperative.",
                                  "genre": "Cooperatif",
                                  "minPlayers": 2,
                                  "maxPlayers": 4,
                                  "durationMinutes": 60,
                                  "editionNumber": 1,
                                  "price": 69.99,
                                  "categoryId": %d,
                                  "publisherId": null,
                                  "authorIds": []
                                }
                                """.formatted(categoryId)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString());

        long wishlistId = extractId(mockMvc.perform(post("/api/wishlists")
                        .with(user("admin@gamesup.test").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "userId": %d,
                                  "gameIds": [%d]
                                }
                                """.formatted(userId, gameId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.games", hasSize(1)))
                .andReturn().getResponse().getContentAsString());

        mockMvc.perform(put("/api/wishlists/" + wishlistId)
                        .with(user("admin@gamesup.test").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "userId": %d,
                                  "gameIds": []
                                }
                                """.formatted(userId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.games", hasSize(0)));

        mockMvc.perform(post("/api/reviews")
                        .with(user("workflow@gamesup.test").roles("CLIENT"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "userId": %d,
                                  "gameId": %d,
                                  "rating": 5,
                                  "comment": "Tres bon jeu cooperatif."
                                }
                                """.formatted(userId, gameId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rating").value(5));

        mockMvc.perform(put("/api/inventory")
                        .with(user("admin@gamesup.test").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "gameId": %d,
                                  "quantity": 7
                                }
                                """.formatted(gameId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(7));

        long purchaseId = extractId(mockMvc.perform(post("/api/purchases")
                        .with(user("admin@gamesup.test").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "userId": %d,
                                  "lines": [
                                    {
                                      "gameId": %d,
                                      "quantity": 2
                                    }
                                  ]
                                }
                                """.formatted(userId, gameId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.lines", hasSize(1)))
                .andReturn().getResponse().getContentAsString());

        mockMvc.perform(patch("/api/purchases/" + purchaseId + "/paid")
                        .with(user("admin@gamesup.test").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paid").value(true));

        mockMvc.perform(get("/api/purchases?userId=" + userId)
                        .with(user("admin@gamesup.test").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}

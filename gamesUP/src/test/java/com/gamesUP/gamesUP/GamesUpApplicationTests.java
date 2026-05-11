package com.gamesUP.gamesUP;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
class GamesUpApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    void publicHealthEndpointIsAvailable() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("GamesUP API is running"));

        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    void anonymousUserCanCreateClientAccount() throws Exception {
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "firstName": "Alice",
                                  "lastName": "Martin",
                                  "email": "alice@gamesup.test",
                                  "password": "secret123"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("alice@gamesup.test"))
                .andExpect(jsonPath("$.role").value("CLIENT"));
    }

    @Test
    void adminCanCreateReferenceAndGameThenSearchIt() throws Exception {
        long categoryId = extractId(mockMvc.perform(post("/api/categories")
                        .with(user("admin@gamesup.test").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Expert\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Expert"))
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
    void adminWorkflowCoversReferencesInventoryReviewsAndPurchases() throws Exception {
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

        long publisherId = extractId(mockMvc.perform(post("/api/publishers")
                        .with(user("admin@gamesup.test").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Workflow Publisher\"}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString());

        long authorId = extractId(mockMvc.perform(post("/api/authors")
                        .with(user("admin@gamesup.test").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Workflow Author\"}"))
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
                                  "publisherId": %d,
                                  "authorIds": [%d]
                                }
                                """.formatted(categoryId, publisherId, authorId)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString());

        mockMvc.perform(get("/api/categories")).andExpect(status().isOk());
        mockMvc.perform(get("/api/publishers")).andExpect(status().isOk());
        mockMvc.perform(get("/api/authors")).andExpect(status().isOk());

        mockMvc.perform(get("/api/games/" + gameId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(gameId));

        mockMvc.perform(put("/api/games/" + gameId)
                        .with(user("admin@gamesup.test").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Pandemic Legacy Saison 1",
                                  "description": "Campagne cooperative mise a jour.",
                                  "genre": "Cooperatif",
                                  "minPlayers": 2,
                                  "maxPlayers": 4,
                                  "durationMinutes": 60,
                                  "editionNumber": 2,
                                  "price": 59.99,
                                  "categoryId": %d,
                                  "publisherId": %d,
                                  "authorIds": [%d]
                                }
                                """.formatted(categoryId, publisherId, authorId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.editionNumber").value(2));

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

        mockMvc.perform(get("/api/wishlists/" + wishlistId)
                        .with(user("admin@gamesup.test").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(wishlistId));

        mockMvc.perform(get("/api/wishlists/users/" + userId)
                        .with(user("admin@gamesup.test").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.id").value(userId));

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

        mockMvc.perform(get("/api/wishlists")
                        .with(user("admin@gamesup.test").roles("ADMIN")))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/wishlists/" + wishlistId)
                        .with(user("admin@gamesup.test").roles("ADMIN")))
                .andExpect(status().isNoContent());

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

        mockMvc.perform(get("/api/reviews?gameId=" + gameId)
                        .with(user("admin@gamesup.test").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

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

        mockMvc.perform(get("/api/inventory")
                        .with(user("admin@gamesup.test").roles("ADMIN")))
                .andExpect(status().isOk());

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

        mockMvc.perform(get("/api/purchases/" + purchaseId)
                        .with(user("admin@gamesup.test").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(purchaseId));

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch("/api/purchases/" + purchaseId + "/paid")
                        .with(user("admin@gamesup.test").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paid").value(true));

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch("/api/purchases/" + purchaseId + "/delivered")
                        .with(user("admin@gamesup.test").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.delivered").value(true));

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch("/api/purchases/" + purchaseId + "/archive")
                        .with(user("admin@gamesup.test").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.archived").value(true));

        mockMvc.perform(get("/api/purchases?userId=" + userId)
                        .with(user("admin@gamesup.test").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        mockMvc.perform(get("/api/purchases")
                        .with(user("admin@gamesup.test").roles("ADMIN")))
                .andExpect(status().isOk());
    }

    @Test
    void adminCanUpdateAndDeleteStandaloneReferences() throws Exception {
        long categoryId = extractId(mockMvc.perform(post("/api/categories")
                        .with(user("admin@gamesup.test").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Temp Category\"}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString());

        mockMvc.perform(put("/api/categories/" + categoryId)
                        .with(user("admin@gamesup.test").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Category\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Category"));

        mockMvc.perform(delete("/api/categories/" + categoryId)
                        .with(user("admin@gamesup.test").roles("ADMIN")))
                .andExpect(status().isNoContent());

        long publisherId = extractId(mockMvc.perform(post("/api/publishers")
                        .with(user("admin@gamesup.test").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Temp Publisher\"}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString());

        mockMvc.perform(put("/api/publishers/" + publisherId)
                        .with(user("admin@gamesup.test").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Publisher\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Publisher"));

        mockMvc.perform(delete("/api/publishers/" + publisherId)
                        .with(user("admin@gamesup.test").roles("ADMIN")))
                .andExpect(status().isNoContent());

        long authorId = extractId(mockMvc.perform(post("/api/authors")
                        .with(user("admin@gamesup.test").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Temp Author\"}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString());

        mockMvc.perform(put("/api/authors/" + authorId)
                        .with(user("admin@gamesup.test").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Author\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Author"));

        mockMvc.perform(delete("/api/authors/" + authorId)
                        .with(user("admin@gamesup.test").roles("ADMIN")))
                .andExpect(status().isNoContent());
    }

    @Test
    void adminCanManageUsersAndDeleteGameAndReview() throws Exception {
        long userId = extractId(mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "firstName": "Managed",
                                  "lastName": "User",
                                  "email": "managed@gamesup.test",
                                  "password": "managed123"
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString());

        mockMvc.perform(get("/api/users")
                        .with(user("admin@gamesup.test").roles("ADMIN")))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/users/" + userId)
                        .with(user("admin@gamesup.test").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("managed@gamesup.test"));

        mockMvc.perform(put("/api/users/" + userId)
                        .with(user("admin@gamesup.test").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "firstName": "Managed",
                                  "lastName": "Admin",
                                  "email": "managed-updated@gamesup.test",
                                  "role": "ADMIN"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("ADMIN"));

        long categoryId = extractId(mockMvc.perform(post("/api/categories")
                        .with(user("admin@gamesup.test").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Suppression\"}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString());

        long gameId = extractId(mockMvc.perform(post("/api/games")
                        .with(user("admin@gamesup.test").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Jeu a supprimer",
                                  "description": "Test suppression.",
                                  "genre": "Test",
                                  "minPlayers": 1,
                                  "maxPlayers": 2,
                                  "durationMinutes": 20,
                                  "editionNumber": 1,
                                  "price": 12.99,
                                  "categoryId": %d,
                                  "publisherId": null,
                                  "authorIds": []
                                }
                                """.formatted(categoryId)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString());

        long reviewId = extractId(mockMvc.perform(post("/api/reviews")
                        .with(user("managed-updated@gamesup.test").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "userId": %d,
                                  "gameId": %d,
                                  "rating": 4,
                                  "comment": "Avis a supprimer."
                                }
                                """.formatted(userId, gameId)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString());

        mockMvc.perform(get("/api/reviews")
                        .with(user("admin@gamesup.test").roles("ADMIN")))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/reviews?userId=" + userId)
                        .with(user("admin@gamesup.test").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        mockMvc.perform(delete("/api/reviews/" + reviewId)
                        .with(user("admin@gamesup.test").roles("ADMIN")))
                .andExpect(status().isNoContent());

        mockMvc.perform(delete("/api/games/" + gameId)
                        .with(user("admin@gamesup.test").roles("ADMIN")))
                .andExpect(status().isNoContent());

        mockMvc.perform(delete("/api/users/" + userId)
                        .with(user("admin@gamesup.test").roles("ADMIN")))
                .andExpect(status().isNoContent());
    }

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
    void recommendationEndpointsBuildUserSignalsBeforeCallingPython() throws Exception {
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

        mockMvc.perform(post("/api/reviews")
                        .with(user("reco@gamesup.test").roles("CLIENT"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "userId": %d,
                                  "gameId": %d,
                                  "rating": 5,
                                  "comment": "Signal positif."
                                }
                                """.formatted(userId, gameId)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/recommendations/users/" + userId)
                        .with(user("admin@gamesup.test").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].gameName").value("Azul"));

        mockMvc.perform(get("/api/recommendations/me")
                        .with(user("reco@gamesup.test").roles("CLIENT")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].gameName").value("Azul"));

        mockMvc.perform(get("/api/recommendations/users/99999")
                        .with(user("admin@gamesup.test").roles("ADMIN")))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/api/recommendations/me")
                        .with(user("ghost@gamesup.test").roles("CLIENT")))
                .andExpect(status().isNotFound());
    }

    @Test
    void gameResponseCoversNullableCategoryAndExistingInventory() throws Exception {
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

    @Test
    void apiReturnsStructuredErrors() throws Exception {
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

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "firstName": "Duplicate",
                                  "lastName": "User",
                                  "email": "duplicate@gamesup.test",
                                  "password": "secret123"
                                }
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "firstName": "Duplicate",
                                  "lastName": "User",
                                  "email": "duplicate@gamesup.test",
                                  "password": "secret123"
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "firstName": "No",
                                  "lastName": "Password",
                                  "email": "no-password@gamesup.test"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));

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

    @Test
    void notFoundBranchesAreHandledAcrossServices() throws Exception {
        mockMvc.perform(get("/api/users/99999")
                        .with(user("admin@gamesup.test").roles("ADMIN")))
                .andExpect(status().isNotFound());

        mockMvc.perform(put("/api/categories/99999")
                        .with(user("admin@gamesup.test").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Missing\"}"))
                .andExpect(status().isNotFound());

        mockMvc.perform(delete("/api/categories/99999")
                        .with(user("admin@gamesup.test").roles("ADMIN")))
                .andExpect(status().isNotFound());

        mockMvc.perform(put("/api/inventory")
                        .with(user("admin@gamesup.test").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "gameId": 99999,
                                  "quantity": 3
                                }
                                """))
                .andExpect(status().isNotFound());

        mockMvc.perform(post("/api/reviews")
                        .with(user("admin@gamesup.test").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "userId": 99999,
                                  "gameId": 99999,
                                  "rating": 3,
                                  "comment": "Invalide."
                                }
                                """))
                .andExpect(status().isNotFound());

        long userId = extractId(mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "firstName": "Error",
                                  "lastName": "Path",
                                  "email": "error-path@gamesup.test",
                                  "password": "error123"
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString());

        mockMvc.perform(post("/api/reviews")
                        .with(user("admin@gamesup.test").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "userId": %d,
                                  "gameId": 99999,
                                  "rating": 3,
                                  "comment": "Jeu invalide."
                                }
                                """.formatted(userId)))
                .andExpect(status().isNotFound());

        mockMvc.perform(delete("/api/reviews/99999")
                        .with(user("admin@gamesup.test").roles("ADMIN")))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/api/wishlists/99999")
                        .with(user("admin@gamesup.test").roles("ADMIN")))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/api/wishlists/users/99999")
                        .with(user("admin@gamesup.test").roles("ADMIN")))
                .andExpect(status().isNotFound());

        mockMvc.perform(post("/api/wishlists")
                        .with(user("admin@gamesup.test").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "userId": 99999,
                                  "gameIds": []
                                }
                                """))
                .andExpect(status().isNotFound());

        mockMvc.perform(post("/api/purchases")
                        .with(user("admin@gamesup.test").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "userId": 99999,
                                  "lines": [
                                    {
                                      "gameId": 99999,
                                      "quantity": 1
                                    }
                                  ]
                                }
                                """))
                .andExpect(status().isNotFound());

        mockMvc.perform(post("/api/purchases")
                        .with(user("admin@gamesup.test").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "userId": %d,
                                  "lines": [
                                    {
                                      "gameId": 99999,
                                      "quantity": 1
                                    }
                                  ]
                                }
                                """.formatted(userId)))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/api/purchases/99999")
                        .with(user("admin@gamesup.test").roles("ADMIN")))
                .andExpect(status().isNotFound());

        mockMvc.perform(post("/api/wishlists")
                        .with(user("admin@gamesup.test").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "userId": %d,
                                  "gameIds": [99999]
                                }
                                """.formatted(userId)))
                .andExpect(status().isNotFound());

        mockMvc.perform(post("/api/games")
                        .with(user("admin@gamesup.test").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Invalid Publisher Game",
                                  "description": "Editeur invalide.",
                                  "genre": "Erreur",
                                  "minPlayers": 1,
                                  "maxPlayers": 2,
                                  "durationMinutes": 10,
                                  "editionNumber": 1,
                                  "price": 1.00,
                                  "categoryId": null,
                                  "publisherId": 99999,
                                  "authorIds": []
                                }
                                """))
                .andExpect(status().isNotFound());

        mockMvc.perform(post("/api/games")
                        .with(user("admin@gamesup.test").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Invalid Author Game",
                                  "description": "Auteur invalide.",
                                  "genre": "Erreur",
                                  "minPlayers": 1,
                                  "maxPlayers": 2,
                                  "durationMinutes": 10,
                                  "editionNumber": 1,
                                  "price": 1.00,
                                  "categoryId": null,
                                  "publisherId": null,
                                  "authorIds": [99999]
                                }
                                """))
                .andExpect(status().isNotFound());
    }

    @Test
    void securityRejectsUnknownBasicUser() throws Exception {
        mockMvc.perform(get("/api/users/me")
                        .with(httpBasic("unknown@gamesup.test", "bad-password")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void protectedAdminRouteRejectsClientRole() throws Exception {
        mockMvc.perform(get("/api/users")
                        .with(user("client@gamesup.test").roles("CLIENT")))
                .andExpect(status().isForbidden());
    }

    @Test
    void httpBasicLoadsUserFromDatabase() throws Exception {
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "firstName": "Basic",
                                  "lastName": "User",
                                  "email": "basic@gamesup.test",
                                  "password": "basic123"
                                }
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/users/me").with(httpBasic("basic@gamesup.test", "basic123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("basic@gamesup.test"));
    }

    private long extractId(String json) throws Exception {
        JsonNode node = objectMapper.readTree(json);
        return node.get("id").asLong();
    }

    private void mockPythonRecommendation() throws Exception {
        Object recommendation = Class.forName("com.gamesUP.gamesUP.dto.RecommendationResponse")
                .getConstructor(Long.class, String.class, double.class, String.class)
                .newInstance(106L, "Azul", 0.98, "KNN demo");
        ResponseEntity rawResponse = ResponseEntity.ok(List.of(recommendation));
        doReturn(rawResponse).when(restTemplate).exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(),
                any(ParameterizedTypeReference.class)
        );
    }
}

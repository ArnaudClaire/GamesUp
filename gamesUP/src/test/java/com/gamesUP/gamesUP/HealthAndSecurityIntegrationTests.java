package com.gamesUP.gamesUP;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

/**
 * Tests d'intégration des endpoints publics et des règles de sécurité.
 */
class HealthAndSecurityIntegrationTests extends AbstractApiIntegrationTest {

    /**
     * Vérifie que les endpoints de santé publics répondent correctement.
     *
     * @throws Exception si l'appel MockMvc échoue.
     */
    @Test
    void publicHealthEndpointIsAvailable() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("GamesUP API is running"));

        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }

    /**
     * Vérifie qu'un visiteur non authentifié peut créer un compte client.
     *
     * @throws Exception si l'appel MockMvc échoue.
     */
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

    /**
     * Vérifie que les routes protégées refusent les rôles ou identifiants invalides.
     *
     * @throws Exception si l'appel MockMvc échoue.
     */
    @Test
    void protectedRoutesApplyExpectedSecurityRules() throws Exception {
        mockMvc.perform(get("/api/users")
                        .with(user("client@gamesup.test").roles("CLIENT")))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/api/users/me")
                        .with(httpBasic("unknown@gamesup.test", "bad-password")))
                .andExpect(status().isUnauthorized());
    }

    /**
     * Vérifie que l'authentification HTTP Basic charge bien l'utilisateur depuis la base.
     *
     * @throws Exception si l'appel MockMvc échoue.
     */
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
}

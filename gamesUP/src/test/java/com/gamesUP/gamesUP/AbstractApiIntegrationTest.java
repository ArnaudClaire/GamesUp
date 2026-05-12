package com.gamesUP.gamesUP;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

/**
 * Classe de base des tests d'intégration HTTP de l'API GamesUP.
 */
@SpringBootTest
@AutoConfigureMockMvc
abstract class AbstractApiIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    protected RestTemplate restTemplate;

    /**
     * Extrait l'identifiant technique renvoyé dans une réponse JSON.
     *
     * @param json corps JSON retourné par l'API.
     * @return identifiant contenu dans la propriété {@code id}.
     * @throws Exception en cas d'erreur de lecture JSON.
     */
    protected long extractId(String json) throws Exception {
        JsonNode node = objectMapper.readTree(json);
        return node.get("id").asLong();
    }

    /**
     * Simule la réponse du service Python de recommandation pour isoler les tests Java.
     *
     * @throws Exception si le DTO de recommandation ne peut pas être instancié.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected void mockPythonRecommendation() throws Exception {
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

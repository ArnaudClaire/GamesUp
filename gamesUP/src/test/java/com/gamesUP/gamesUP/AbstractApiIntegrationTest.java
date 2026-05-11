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

@SpringBootTest
@AutoConfigureMockMvc
abstract class AbstractApiIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    protected RestTemplate restTemplate;

    protected long extractId(String json) throws Exception {
        JsonNode node = objectMapper.readTree(json);
        return node.get("id").asLong();
    }

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

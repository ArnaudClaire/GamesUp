package com.gamesUP.gamesUP;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DtoContractUnitTests {

    @Test
    void gameDtoContractsExposeExpectedValues() throws Exception {
        Class<?> requestType = Class.forName("com.gamesUP.gamesUP.dto.GameRequest");
        Constructor<?> requestConstructor = requestType.getConstructor(
                String.class,
                String.class,
                String.class,
                Integer.class,
                Integer.class,
                Integer.class,
                Integer.class,
                BigDecimal.class,
                Long.class,
                Long.class,
                Set.class
        );

        Object request = requestConstructor.newInstance(
                "Catan",
                "Gestion de ressources.",
                "Strategie",
                3,
                4,
                90,
                1,
                BigDecimal.valueOf(39.99),
                1L,
                2L,
                Set.of(3L)
        );

        assertThat(requestType.getMethod("name").invoke(request)).isEqualTo("Catan");
        assertThat(requestType.getMethod("categoryId").invoke(request)).isEqualTo(1L);
        assertThat(requestType.getMethod("authorIds").invoke(request)).isEqualTo(Set.of(3L));
    }

    @Test
    void recommendationResponseContractExposesExpectedValues() throws Exception {
        Class<?> responseType = Class.forName("com.gamesUP.gamesUP.dto.RecommendationResponse");
        Object response = responseType
                .getConstructor(Long.class, String.class, double.class, String.class)
                .newInstance(106L, "Azul", 0.98, "KNN demo");

        assertThat(responseType.getMethod("gameId").invoke(response)).isEqualTo(106L);
        assertThat(responseType.getMethod("gameName").invoke(response)).isEqualTo("Azul");
        assertThat(responseType.getMethod("score").invoke(response)).isEqualTo(0.98);
        assertThat(responseType.getMethod("reason").invoke(response)).isEqualTo("KNN demo");
    }
}

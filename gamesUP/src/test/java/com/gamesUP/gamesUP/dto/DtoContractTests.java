package com.gamesUP.gamesUP.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DtoContractTests {

    @Test
    void gameRequestExposeLesChampsEnvoyesParLeClient() throws Exception {
        Object request = newRecord("GameRequest",
                constructorTypes(String.class, String.class, String.class, Integer.class, Integer.class,
                        Integer.class, Integer.class, BigDecimal.class, Long.class, Long.class, Set.class),
                "Catan", "Gestion de ressources.", "Strategie", 3, 4, 90, 1,
                BigDecimal.valueOf(39.99), 1L, 2L, Set.of(3L));

        assertThat(field(request, "name")).isEqualTo("Catan");
        assertThat(field(request, "description")).isEqualTo("Gestion de ressources.");
        assertThat(field(request, "genre")).isEqualTo("Strategie");
        assertThat(field(request, "minPlayers")).isEqualTo(3);
        assertThat(field(request, "maxPlayers")).isEqualTo(4);
        assertThat(field(request, "durationMinutes")).isEqualTo(90);
        assertThat(field(request, "editionNumber")).isEqualTo(1);
        assertThat((BigDecimal) field(request, "price")).isEqualByComparingTo("39.99");
        assertThat(field(request, "categoryId")).isEqualTo(1L);
        assertThat(field(request, "publisherId")).isEqualTo(2L);
        assertThat(field(request, "authorIds")).isEqualTo(Set.of(3L));
    }

    @Test
    void gameResponseExposeLesChampsRetournesParLeCatalogue() throws Exception {
        Object category = reference(1L, "Strategie");
        Object publisher = reference(2L, "GamesUP Editions");
        Object author = reference(3L, "Auteur Demo");

        Object response = newRecord("GameResponse",
                constructorTypes(Long.class, String.class, String.class, String.class, Integer.class, Integer.class,
                        Integer.class, Integer.class, BigDecimal.class, dto("ReferenceResponse"),
                        dto("ReferenceResponse"), Set.class, Integer.class),
                10L, "Catan", "Gestion de ressources.", "Strategie", 3, 4, 90, 1,
                BigDecimal.valueOf(39.99), category, publisher, Set.of(author), 12);

        assertThat(field(response, "id")).isEqualTo(10L);
        assertThat(field(response, "name")).isEqualTo("Catan");
        assertThat(field(response, "category")).isEqualTo(category);
        assertThat(field(response, "publisher")).isEqualTo(publisher);
        assertThat(field(response, "authors")).isEqualTo(Set.of(author));
        assertThat(field(response, "stockQuantity")).isEqualTo(12);
    }

    @Test
    void userDtoExposeLesInformationsDeCompteSansAmbiguite() throws Exception {
        Object adminRole = Enum.valueOf((Class<Enum>) model("Role"), "ADMIN");

        Object request = newRecord("UserRequest",
                constructorTypes(String.class, String.class, String.class, String.class, model("Role")),
                "Alice", "Martin", "alice@gamesup.test", "secret123", adminRole);
        Object response = newRecord("UserResponse",
                constructorTypes(Long.class, String.class, String.class, String.class, model("Role")),
                1L, "Alice", "Martin", "alice@gamesup.test", adminRole);

        assertThat(field(request, "firstName")).isEqualTo("Alice");
        assertThat(field(request, "lastName")).isEqualTo("Martin");
        assertThat(field(request, "email")).isEqualTo("alice@gamesup.test");
        assertThat(field(request, "password")).isEqualTo("secret123");
        assertThat(field(request, "role")).isEqualTo(adminRole);

        assertThat(field(response, "id")).isEqualTo(1L);
        assertThat(field(response, "firstName")).isEqualTo("Alice");
        assertThat(field(response, "lastName")).isEqualTo("Martin");
        assertThat(field(response, "email")).isEqualTo("alice@gamesup.test");
        assertThat(field(response, "role")).isEqualTo(adminRole);
    }

    @Test
    void purchaseDtoExposeLesLignesEtLeStatutDeCommande() throws Exception {
        Object lineRequest = newRecord("PurchaseLineRequest",
                constructorTypes(Long.class, int.class),
                10L, 2);
        Object request = newRecord("PurchaseRequest",
                constructorTypes(Long.class, List.class),
                1L, List.of(lineRequest));

        Object user = reference(1L, "alice@gamesup.test");
        Object lineResponse = newRecord("PurchaseLineResponse",
                constructorTypes(Long.class, Long.class, String.class, int.class, BigDecimal.class),
                20L, 10L, "Catan", 2, BigDecimal.valueOf(39.99));
        LocalDateTime date = LocalDateTime.of(2026, 5, 11, 12, 0);
        Object response = newRecord("PurchaseResponse",
                constructorTypes(Long.class, dto("ReferenceResponse"), List.class, LocalDateTime.class,
                        boolean.class, boolean.class, boolean.class),
                30L, user, List.of(lineResponse), date, true, false, false);

        assertThat(field(request, "userId")).isEqualTo(1L);
        assertThat(field(request, "lines")).isEqualTo(List.of(lineRequest));
        assertThat(field(lineRequest, "gameId")).isEqualTo(10L);
        assertThat(field(lineRequest, "quantity")).isEqualTo(2);

        assertThat(field(response, "id")).isEqualTo(30L);
        assertThat(field(response, "user")).isEqualTo(user);
        assertThat(field(response, "lines")).isEqualTo(List.of(lineResponse));
        assertThat(field(response, "date")).isEqualTo(date);
        assertThat(field(response, "paid")).isEqualTo(true);
        assertThat(field(response, "delivered")).isEqualTo(false);
        assertThat(field(response, "archived")).isEqualTo(false);
        assertThat((BigDecimal) field(lineResponse, "unitPrice")).isEqualByComparingTo("39.99");
    }

    @Test
    void autresDtoRestentLisiblesEtExposentLeursContrats() throws Exception {
        Object referenceRequest = newRecord("ReferenceRequest", constructorTypes(String.class), "Strategie");
        Object referenceResponse = reference(1L, "Strategie");
        Object inventoryRequest = newRecord("InventoryRequest", constructorTypes(Long.class, int.class), 10L, 12);
        Object inventoryResponse = newRecord("InventoryResponse",
                constructorTypes(Long.class, Long.class, String.class, int.class), 2L, 10L, "Catan", 12);
        Object reviewRequest = newRecord("ReviewRequest",
                constructorTypes(Long.class, Long.class, int.class, String.class), 1L, 10L, 5, "Tres bon jeu.");
        Object reviewResponse = newRecord("ReviewResponse",
                constructorTypes(Long.class, Long.class, String.class, Long.class, String.class, int.class, String.class),
                3L, 1L, "alice@gamesup.test", 10L, "Catan", 5, "Tres bon jeu.");
        Object wishlistRequest = newRecord("WishlistRequest", constructorTypes(Long.class, Set.class), 1L, Set.of(10L));
        Object wishlistResponse = newRecord("WishlistResponse",
                constructorTypes(Long.class, dto("ReferenceResponse"), Set.class), 4L, referenceResponse, Set.of(referenceResponse));
        Object recommendationPurchase = newRecord("RecommendationPurchaseRequest",
                constructorTypes(Long.class, double.class), 10L, 4.5);
        Object recommendationRequest = newRecord("RecommendationRequest",
                constructorTypes(Long.class, List.class), 1L, List.of(recommendationPurchase));
        Object recommendationResponse = newRecord("RecommendationResponse",
                constructorTypes(Long.class, String.class, double.class, String.class), 106L, "Azul", 0.98, "KNN demo");

        assertThat(field(referenceRequest, "name")).isEqualTo("Strategie");
        assertThat(field(referenceResponse, "name")).isEqualTo("Strategie");
        assertThat(field(inventoryRequest, "quantity")).isEqualTo(12);
        assertThat(field(inventoryResponse, "gameName")).isEqualTo("Catan");
        assertThat(field(reviewRequest, "rating")).isEqualTo(5);
        assertThat(field(reviewResponse, "userEmail")).isEqualTo("alice@gamesup.test");
        assertThat(field(wishlistRequest, "gameIds")).isEqualTo(Set.of(10L));
        assertThat(field(wishlistResponse, "games")).isEqualTo(Set.of(referenceResponse));
        assertThat(field(recommendationRequest, "purchases")).isEqualTo(List.of(recommendationPurchase));
        assertThat(field(recommendationResponse, "gameId")).isEqualTo(106L);
        assertThat(field(recommendationResponse, "gameName")).isEqualTo("Azul");
        assertThat(field(recommendationResponse, "score")).isEqualTo(0.98);
        assertThat(field(recommendationResponse, "reason")).isEqualTo("KNN demo");
    }

    private Object reference(Long id, String name) throws Exception {
        return newRecord("ReferenceResponse", constructorTypes(Long.class, String.class), id, name);
    }

    private Object newRecord(String simpleName, Class<?>[] parameterTypes, Object... values) throws Exception {
        Constructor<?> constructor = dto(simpleName).getConstructor(parameterTypes);
        return constructor.newInstance(values);
    }

    private Class<?>[] constructorTypes(Class<?>... types) {
        return types;
    }

    private Object field(Object record, String fieldName) throws Exception {
        return record.getClass().getMethod(fieldName).invoke(record);
    }

    private Class<?> dto(String simpleName) throws Exception {
        return Class.forName("com.gamesUP.gamesUP.dto." + simpleName);
    }

    private Class<?> model(String simpleName) throws Exception {
        return Class.forName("com.gamesUP.gamesUP.model." + simpleName);
    }
}

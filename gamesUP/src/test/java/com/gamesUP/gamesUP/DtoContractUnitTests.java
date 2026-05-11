package com.gamesUP.gamesUP;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    @Test
    void resourceNotFoundExceptionFormatsLabelAndId() throws Exception {
        Class<?> exceptionType = Class.forName("com.gamesUP.gamesUP.exception.ResourceNotFoundException");
        Object exception = exceptionType
                .getConstructor(String.class, Long.class)
                .newInstance("Game", 42L);

        assertThat(((RuntimeException) exception).getMessage()).isEqualTo("Game not found with id 42");
    }

    @Test
    void modelAccessorsExposeExpectedValues() throws Exception {
        Object category = newModel("Category");
        call(category, "setId", Long.class, 1L);
        call(category, "setName", String.class, "Strategie");

        Object publisher = newModel("Publisher");
        call(publisher, "setId", Long.class, 2L);
        call(publisher, "setName", String.class, "GamesUP Editions");

        Object author = newModel("Author");
        call(author, "setId", Long.class, 3L);
        call(author, "setName", String.class, "Auteur Demo");

        Object user = newModel("User");
        call(user, "setId", Long.class, 4L);
        call(user, "setFirstName", String.class, "Client");
        call(user, "setLastName", String.class, "Demo");
        call(user, "setEmail", String.class, "client@gamesup.test");
        call(user, "setPassword", String.class, "secret");
        Object adminRole = Enum.valueOf((Class<Enum>) Class.forName("com.gamesUP.gamesUP.model.Role"), "ADMIN");
        call(user, "setRole", Class.forName("com.gamesUP.gamesUP.model.Role"), adminRole);

        Object game = newModel("Game");
        call(game, "setId", Long.class, 5L);
        call(game, "setName", String.class, "Catan");
        call(game, "setDescription", String.class, "Gestion de ressources.");
        call(game, "setGenre", String.class, "Strategie");
        call(game, "setMinPlayers", Integer.class, 3);
        call(game, "setMaxPlayers", Integer.class, 4);
        call(game, "setDurationMinutes", Integer.class, 90);
        call(game, "setEditionNumber", Integer.class, 1);
        call(game, "setPrice", BigDecimal.class, BigDecimal.valueOf(39.99));
        call(game, "setCategory", Class.forName("com.gamesUP.gamesUP.model.Category"), category);
        call(game, "setPublisher", Class.forName("com.gamesUP.gamesUP.model.Publisher"), publisher);
        call(game, "setAuthors", Set.class, Set.of(author));

        Object inventory = newModel("Inventory");
        call(inventory, "setId", Long.class, 6L);
        call(inventory, "setGame", Class.forName("com.gamesUP.gamesUP.model.Game"), game);
        call(inventory, "setQuantity", int.class, 12);
        call(game, "setInventory", Class.forName("com.gamesUP.gamesUP.model.Inventory"), inventory);

        Object avis = newModel("Avis");
        call(avis, "setId", Long.class, 7L);
        call(avis, "setComment", String.class, "Tres bon jeu.");
        call(avis, "setRating", int.class, 5);
        call(avis, "setUser", Class.forName("com.gamesUP.gamesUP.model.User"), user);
        call(avis, "setGame", Class.forName("com.gamesUP.gamesUP.model.Game"), game);

        Object purchase = newModel("Purchase");
        call(purchase, "setId", Long.class, 8L);
        call(purchase, "setUser", Class.forName("com.gamesUP.gamesUP.model.User"), user);
        call(purchase, "setDate", LocalDateTime.class, LocalDateTime.of(2026, 5, 11, 12, 0));
        call(purchase, "setPaid", boolean.class, true);
        call(purchase, "setDelivered", boolean.class, true);
        call(purchase, "setArchived", boolean.class, false);

        Object line = newModel("PurchaseLine");
        call(line, "setId", Long.class, 9L);
        call(line, "setPurchase", Class.forName("com.gamesUP.gamesUP.model.Purchase"), purchase);
        call(line, "setGame", Class.forName("com.gamesUP.gamesUP.model.Game"), game);
        call(line, "setQuantity", int.class, 2);
        call(line, "setUnitPrice", BigDecimal.class, BigDecimal.valueOf(39.99));
        call(purchase, "setLines", java.util.List.class, java.util.List.of(line));

        Object wishlist = newModel("Wishlist");
        call(wishlist, "setId", Long.class, 10L);
        call(wishlist, "setUser", Class.forName("com.gamesUP.gamesUP.model.User"), user);
        call(wishlist, "setGames", Set.class, Set.of(game));
        call(author, "setGames", Set.class, Set.of(game));

        assertThat(call(category, "getId")).isEqualTo(1L);
        assertThat(call(category, "getName")).isEqualTo("Strategie");
        assertThat(call(publisher, "getId")).isEqualTo(2L);
        assertThat(call(publisher, "getName")).isEqualTo("GamesUP Editions");
        assertThat(call(author, "getId")).isEqualTo(3L);
        assertThat(call(author, "getName")).isEqualTo("Auteur Demo");
        assertThat(((Set<?>) call(author, "getGames")).iterator().next()).isEqualTo(game);
        assertThat(call(user, "getId")).isEqualTo(4L);
        assertThat(call(user, "getFirstName")).isEqualTo("Client");
        assertThat(call(user, "getLastName")).isEqualTo("Demo");
        assertThat(call(user, "getEmail")).isEqualTo("client@gamesup.test");
        assertThat(call(user, "getPassword")).isEqualTo("secret");
        assertThat(call(user, "getRole")).isEqualTo(adminRole);
        assertThat(call(game, "getId")).isEqualTo(5L);
        assertThat(call(game, "getName")).isEqualTo("Catan");
        assertThat(call(game, "getDescription")).isEqualTo("Gestion de ressources.");
        assertThat(call(game, "getGenre")).isEqualTo("Strategie");
        assertThat(call(game, "getMinPlayers")).isEqualTo(3);
        assertThat(call(game, "getMaxPlayers")).isEqualTo(4);
        assertThat(call(game, "getDurationMinutes")).isEqualTo(90);
        assertThat(call(game, "getEditionNumber")).isEqualTo(1);
        assertThat((BigDecimal) call(game, "getPrice")).isEqualByComparingTo("39.99");
        assertThat(call(game, "getCategory")).isEqualTo(category);
        assertThat(call(game, "getPublisher")).isEqualTo(publisher);
        assertThat(((Set<?>) call(game, "getAuthors")).iterator().next()).isEqualTo(author);
        assertThat(call(game, "getInventory")).isEqualTo(inventory);
        assertThat(call(inventory, "getId")).isEqualTo(6L);
        assertThat(call(inventory, "getGame")).isEqualTo(game);
        assertThat(call(inventory, "getQuantity")).isEqualTo(12);
        assertThat(call(avis, "getId")).isEqualTo(7L);
        assertThat(call(avis, "getComment")).isEqualTo("Tres bon jeu.");
        assertThat(call(avis, "getRating")).isEqualTo(5);
        assertThat(call(avis, "getUser")).isEqualTo(user);
        assertThat(call(avis, "getGame")).isEqualTo(game);
        assertThat(call(purchase, "getId")).isEqualTo(8L);
        assertThat(call(purchase, "getUser")).isEqualTo(user);
        assertThat(((java.util.List<?>) call(purchase, "getLines")).get(0)).isEqualTo(line);
        assertThat(call(purchase, "getDate")).isEqualTo(LocalDateTime.of(2026, 5, 11, 12, 0));
        assertThat(call(purchase, "isPaid")).isEqualTo(true);
        assertThat(call(purchase, "isDelivered")).isEqualTo(true);
        assertThat(call(purchase, "isArchived")).isEqualTo(false);
        assertThat(call(line, "getId")).isEqualTo(9L);
        assertThat(call(line, "getPurchase")).isEqualTo(purchase);
        assertThat(call(line, "getGame")).isEqualTo(game);
        assertThat(call(line, "getQuantity")).isEqualTo(2);
        assertThat((BigDecimal) call(line, "getUnitPrice")).isEqualByComparingTo("39.99");
        assertThat(call(wishlist, "getId")).isEqualTo(10L);
        assertThat(call(wishlist, "getUser")).isEqualTo(user);
        assertThat(((Set<?>) call(wishlist, "getGames")).iterator().next()).isEqualTo(game);
    }

    private Object newModel(String simpleName) throws Exception {
        return Class.forName("com.gamesUP.gamesUP.model." + simpleName).getConstructor().newInstance();
    }

    private Object call(Object target, String methodName) throws Exception {
        return target.getClass().getMethod(methodName).invoke(target);
    }

    private void call(Object target, String methodName, Class<?> parameterType, Object value) throws Exception {
        target.getClass().getMethod(methodName, parameterType).invoke(target, value);
    }
}

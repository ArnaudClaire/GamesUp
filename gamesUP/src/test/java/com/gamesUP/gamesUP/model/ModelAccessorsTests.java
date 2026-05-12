package com.gamesUP.gamesUP.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

/**
 * Tests unitaires des accesseurs des modèles JPA.
 */
class ModelAccessorsTests {

    /**
     * Vérifie que les modèles de référence exposent leurs identifiants et libellés.
     *
     * @throws Exception si la construction réflexive d'un modèle échoue.
     */
    @Test
    void referenceModelsExposentLeursIdentifiantsEtLibelles() throws Exception {
        Object category = model("Category");
        set(category, "setId", Long.class, 1L);
        set(category, "setName", String.class, "Strategie");

        Object publisher = model("Publisher");
        set(publisher, "setId", Long.class, 2L);
        set(publisher, "setName", String.class, "GamesUP Editions");

        Object author = model("Author");
        set(author, "setId", Long.class, 3L);
        set(author, "setName", String.class, "Auteur Demo");

        assertThat(get(category, "getId")).isEqualTo(1L);
        assertThat(get(category, "getName")).isEqualTo("Strategie");
        assertThat(get(publisher, "getId")).isEqualTo(2L);
        assertThat(get(publisher, "getName")).isEqualTo("GamesUP Editions");
        assertThat(get(author, "getId")).isEqualTo(3L);
        assertThat(get(author, "getName")).isEqualTo("Auteur Demo");
    }

    /**
     * Vérifie les relations entre utilisateur, jeu, stock, avis, commande et liste d'envies.
     *
     * @throws Exception si l'accès réflexif aux accesseurs échoue.
     */
    @Test
    void userGameInventoryReviewPurchaseEtWishlistExposentLeursRelations() throws Exception {
        Object category = model("Category");
        set(category, "setId", Long.class, 1L);
        set(category, "setName", String.class, "Strategie");

        Object publisher = model("Publisher");
        set(publisher, "setId", Long.class, 2L);
        set(publisher, "setName", String.class, "GamesUP Editions");

        Object author = model("Author");
        set(author, "setId", Long.class, 3L);
        set(author, "setName", String.class, "Auteur Demo");

        Object user = model("User");
        Object adminRole = Enum.valueOf((Class<Enum>) modelType("Role"), "ADMIN");
        set(user, "setId", Long.class, 4L);
        set(user, "setFirstName", String.class, "Client");
        set(user, "setLastName", String.class, "Demo");
        set(user, "setEmail", String.class, "client@gamesup.test");
        set(user, "setPassword", String.class, "secret");
        set(user, "setRole", modelType("Role"), adminRole);

        Object game = model("Game");
        set(game, "setId", Long.class, 5L);
        set(game, "setName", String.class, "Catan");
        set(game, "setDescription", String.class, "Gestion de ressources.");
        set(game, "setGenre", String.class, "Strategie");
        set(game, "setMinPlayers", Integer.class, 3);
        set(game, "setMaxPlayers", Integer.class, 4);
        set(game, "setDurationMinutes", Integer.class, 90);
        set(game, "setEditionNumber", Integer.class, 1);
        set(game, "setPrice", BigDecimal.class, BigDecimal.valueOf(39.99));
        set(game, "setCategory", modelType("Category"), category);
        set(game, "setPublisher", modelType("Publisher"), publisher);
        set(game, "setAuthors", Set.class, Set.of(author));
        set(author, "setGames", Set.class, Set.of(game));

        Object inventory = model("Inventory");
        set(inventory, "setId", Long.class, 6L);
        set(inventory, "setGame", modelType("Game"), game);
        set(inventory, "setQuantity", int.class, 12);
        set(game, "setInventory", modelType("Inventory"), inventory);

        Object avis = model("Avis");
        set(avis, "setId", Long.class, 7L);
        set(avis, "setComment", String.class, "Tres bon jeu.");
        set(avis, "setRating", int.class, 5);
        set(avis, "setUser", modelType("User"), user);
        set(avis, "setGame", modelType("Game"), game);

        Object purchase = model("Purchase");
        set(purchase, "setId", Long.class, 8L);
        set(purchase, "setUser", modelType("User"), user);
        set(purchase, "setDate", LocalDateTime.class, LocalDateTime.of(2026, 5, 11, 12, 0));
        set(purchase, "setPaid", boolean.class, true);
        set(purchase, "setDelivered", boolean.class, true);
        set(purchase, "setArchived", boolean.class, false);

        Object line = model("PurchaseLine");
        set(line, "setId", Long.class, 9L);
        set(line, "setPurchase", modelType("Purchase"), purchase);
        set(line, "setGame", modelType("Game"), game);
        set(line, "setQuantity", int.class, 2);
        set(line, "setUnitPrice", BigDecimal.class, BigDecimal.valueOf(39.99));
        set(purchase, "setLines", List.class, List.of(line));

        Object wishlist = model("Wishlist");
        set(wishlist, "setId", Long.class, 10L);
        set(wishlist, "setUser", modelType("User"), user);
        set(wishlist, "setGames", Set.class, Set.of(game));

        assertThat(get(user, "getRole")).isEqualTo(adminRole);
        assertThat(get(game, "getCategory")).isEqualTo(category);
        assertThat(get(game, "getPublisher")).isEqualTo(publisher);
        assertThat(get(game, "getAuthors")).isEqualTo(Set.of(author));
        assertThat(get(author, "getGames")).isEqualTo(Set.of(game));
        assertThat(get(game, "getInventory")).isEqualTo(inventory);
        assertThat(get(inventory, "getQuantity")).isEqualTo(12);
        assertThat(get(avis, "getUser")).isEqualTo(user);
        assertThat(get(avis, "getGame")).isEqualTo(game);
        assertThat(get(purchase, "getUser")).isEqualTo(user);
        assertThat(get(purchase, "getLines")).isEqualTo(List.of(line));
        assertThat(get(purchase, "isPaid")).isEqualTo(true);
        assertThat(get(purchase, "isDelivered")).isEqualTo(true);
        assertThat(get(purchase, "isArchived")).isEqualTo(false);
        assertThat(get(line, "getPurchase")).isEqualTo(purchase);
        assertThat(get(line, "getGame")).isEqualTo(game);
        assertThat((BigDecimal) get(line, "getUnitPrice")).isEqualByComparingTo("39.99");
        assertThat(get(wishlist, "getUser")).isEqualTo(user);
        assertThat(get(wishlist, "getGames")).isEqualTo(Set.of(game));
    }

    /**
     * Instancie un modèle par son nom simple.
     *
     * @param simpleName nom simple du modèle.
     * @return instance du modèle demandé.
     * @throws Exception si le type ou le constructeur est introuvable.
     */
    private Object model(String simpleName) throws Exception {
        return modelType(simpleName).getConstructor().newInstance();
    }

    /**
     * Charge le type Java d'un modèle par son nom simple.
     *
     * @param simpleName nom simple du modèle.
     * @return classe Java du modèle.
     * @throws Exception si le type est introuvable.
     */
    private Class<?> modelType(String simpleName) throws Exception {
        return Class.forName("com.gamesUP.gamesUP.model." + simpleName);
    }

    /**
     * Appelle un accesseur sans paramètre sur une instance.
     *
     * @param target instance interrogée.
     * @param methodName nom de l'accesseur.
     * @return valeur retournée par l'accesseur.
     * @throws Exception si l'appel réflexif échoue.
     */
    private Object get(Object target, String methodName) throws Exception {
        return target.getClass().getMethod(methodName).invoke(target);
    }

    /**
     * Appelle un mutateur sur une instance.
     *
     * @param target instance modifiée.
     * @param methodName nom du mutateur.
     * @param parameterType type du paramètre attendu.
     * @param value valeur transmise au mutateur.
     * @throws Exception si l'appel réflexif échoue.
     */
    private void set(Object target, String methodName, Class<?> parameterType, Object value) throws Exception {
        target.getClass().getMethod(methodName, parameterType).invoke(target, value);
    }
}

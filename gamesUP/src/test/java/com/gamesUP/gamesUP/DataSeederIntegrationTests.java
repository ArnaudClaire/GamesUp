package com.gamesUP.gamesUP;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import java.util.Optional;

@SpringBootTest(properties = {
        "gamesup.seed-data.enabled=true",
        "spring.datasource.url=jdbc:h2:mem:gamesup-seed-test;DB_CLOSE_DELAY=-1;MODE=MySQL",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class DataSeederIntegrationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void seedDataCreatesDemoUsersCatalogAndInventory() throws Exception {
        Object userRepository = repository("UserRepository");
        Object categoryRepository = repository("CategoryRepository");
        Object gameRepository = repository("GameRepository");
        Object inventoryRepository = repository("InventoryRepository");

        assertThat(count(userRepository)).isEqualTo(2);
        assertThat(count(categoryRepository)).isEqualTo(2);
        assertThat(count(gameRepository)).isEqualTo(2);
        assertThat(count(inventoryRepository)).isEqualTo(2);

        assertThat((Optional<?>) findByEmail(userRepository, "admin@gamesup.local"))
                .isPresent()
                .get()
                .extracting("role")
                .extracting(Object::toString)
                .isEqualTo("ADMIN");

        assertThat((Optional<?>) findByEmail(userRepository, "client@gamesup.local"))
                .isPresent()
                .get()
                .extracting("role")
                .extracting(Object::toString)
                .isEqualTo("CLIENT");
    }

    private Object repository(String simpleName) throws Exception {
        Class<?> type = Class.forName("com.gamesUP.gamesUP.repository." + simpleName);
        return applicationContext.getBean(type);
    }

    private long count(Object repository) throws Exception {
        return (long) repository.getClass().getMethod("count").invoke(repository);
    }

    private Object findByEmail(Object repository, String email) throws Exception {
        return repository.getClass().getMethod("findByEmail", String.class).invoke(repository, email);
    }
}

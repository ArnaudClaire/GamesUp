package com.gamesUP.gamesUP.config;

import com.gamesUP.gamesUP.model.Author;
import com.gamesUP.gamesUP.model.Category;
import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.model.Inventory;
import com.gamesUP.gamesUP.model.Publisher;
import com.gamesUP.gamesUP.model.Role;
import com.gamesUP.gamesUP.model.User;
import com.gamesUP.gamesUP.repository.AuthorRepository;
import com.gamesUP.gamesUP.repository.CategoryRepository;
import com.gamesUP.gamesUP.repository.GameRepository;
import com.gamesUP.gamesUP.repository.InventoryRepository;
import com.gamesUP.gamesUP.repository.PublisherRepository;
import com.gamesUP.gamesUP.repository.UserRepository;
import java.math.BigDecimal;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
/**
 * Inserts a small demonstration dataset when seed data is enabled.
 */
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;
    private final GameRepository gameRepository;
    private final InventoryRepository inventoryRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${gamesup.seed-data.enabled:true}")
    private boolean enabled;

    /**
     * Creates the seeder with all repositories needed by the demo dataset.
     *
     * @param userRepository repository for users
     * @param categoryRepository repository for categories
     * @param authorRepository repository for authors
     * @param publisherRepository repository for publishers
     * @param gameRepository repository for games
     * @param inventoryRepository repository for inventory entries
     * @param passwordEncoder password encoder for demo accounts
     */
    public DataSeeder(
            UserRepository userRepository,
            CategoryRepository categoryRepository,
            AuthorRepository authorRepository,
            PublisherRepository publisherRepository,
            GameRepository gameRepository,
            InventoryRepository inventoryRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.authorRepository = authorRepository;
        this.publisherRepository = publisherRepository;
        this.gameRepository = gameRepository;
        this.inventoryRepository = inventoryRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Seeds the database if it is empty and seed data is enabled.
     *
     * @param args command-line arguments
     */
    @Override
    public void run(String... args) {
        if (!enabled || userRepository.count() > 0 || gameRepository.count() > 0) {
            return;
        }

        User admin = createUser("Admin", "GamesUP", "admin@gamesup.local", "admin123", Role.ADMIN);
        User client = createUser("Client", "Demo", "client@gamesup.local", "client123", Role.CLIENT);
        userRepository.saveAll(Set.of(admin, client));

        Category strategy = saveCategory("Strategie");
        Category family = saveCategory("Famille");
        Publisher publisher = savePublisher("GamesUP Editions");
        Author author = saveAuthor("Auteur Demo");

        Game catan = createGame("Catan", "Gestion de ressources et negociation.", "Strategie", 3, 4, 90, BigDecimal.valueOf(39.99), strategy, publisher, author);
        Game pandemic = createGame("Pandemic", "Jeu cooperatif de lutte contre des epidemies.", "Cooperatif", 2, 4, 45, BigDecimal.valueOf(34.99), family, publisher, author);
        gameRepository.saveAll(Set.of(catan, pandemic));

        saveInventory(catan, 12);
        saveInventory(pandemic, 8);
    }

    private User createUser(String firstName, String lastName, String email, String password, Role role) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        return user;
    }

    private Category saveCategory(String name) {
        Category category = new Category();
        category.setName(name);
        return categoryRepository.save(category);
    }

    private Publisher savePublisher(String name) {
        Publisher publisher = new Publisher();
        publisher.setName(name);
        return publisherRepository.save(publisher);
    }

    private Author saveAuthor(String name) {
        Author author = new Author();
        author.setName(name);
        return authorRepository.save(author);
    }

    private Game createGame(String name, String description, String genre, int minPlayers, int maxPlayers, int duration, BigDecimal price, Category category, Publisher publisher, Author author) {
        Game game = new Game();
        game.setName(name);
        game.setDescription(description);
        game.setGenre(genre);
        game.setMinPlayers(minPlayers);
        game.setMaxPlayers(maxPlayers);
        game.setDurationMinutes(duration);
        game.setEditionNumber(1);
        game.setPrice(price);
        game.setCategory(category);
        game.setPublisher(publisher);
        game.setAuthors(Set.of(author));
        return game;
    }

    private void saveInventory(Game game, int quantity) {
        Inventory inventory = new Inventory();
        inventory.setGame(game);
        inventory.setQuantity(quantity);
        inventoryRepository.save(inventory);
    }
}

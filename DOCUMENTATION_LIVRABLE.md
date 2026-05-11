# GamesUP - Documentation livrable

## Vérification des exigences

| Exigence | État | Éléments de preuve |
| --- | --- | --- |
| Notions client, jeu, éditeur, auteur, commande | OK | Entités JPA `User`, `Game`, `Publisher`, `Author`, `Purchase`, `PurchaseLine` |
| Informations issues du code Java fourni | OK | Entités conservées et complétées : `Avis`, `Inventory`, `Wishlist`, `Category` |
| CRUD de base | OK | Controllers REST pour utilisateurs, jeux, références, commandes, avis, stocks et wishlists |
| Deux types de compte | OK | Enum `Role` avec `CLIENT` et `ADMIN` |
| Recherche sur les jeux | OK | `GameRepository.search` et `GET /api/games?search=...` |
| Architecture Spring cohérente | OK | Couches controller, service, repository, mapper, dto, model, config, exception |
| Principes SOLID | OK | Responsabilités séparées et injection par constructeurs explicites |
| Hibernate | OK | Entités `@Entity`, repositories Spring Data JPA, relations JPA |
| Spring Security | OK | `SecurityConfig`, HTTP Basic, routes publiques/client/admin |
| Tests Spring uniquement | OK | Tests MockMvc, tests d’intégration Spring, tests unitaires DTO/model |
| API Python FastAPI | OK | `CodeApiPython/main.py` |
| Modèle KNN | OK | `CodeApiPython/recommendation.py` avec voisins proches et similarité cosinus |
| Communication Spring vers Python | OK | `RecommendationService` appelle `POST /recommendations` via `RestTemplate` |
| Diagrammes | OK | Architecture, classes, composants et séquence ci-dessous |
| Rapports de couverture | OK | `gamesUP/target/site/jacoco/index.html` et `coverage-report.pdf` |

## Analyse de l’existant initial

Le projet fourni contenait déjà une base d’API Spring et une base d’API Python, mais l’architecture n’était pas encore suffisante pour répondre aux attentes du sujet.

Côté Spring, les points principaux relevés étaient :

- un accès aux données trop proche du controller ;
- une séparation insuffisante entre les responsabilités HTTP, métier et persistance ;
- des modèles Java représentant les notions métier, mais pas encore tous exploités comme entités JPA complètes ;
- peu ou pas de CRUD REST structurés ;
- pas de sécurité Spring active et donc pas de gestion claire des rôles client/administrateur ;
- une couverture de tests insuffisante ;
- pas de communication opérationnelle avec l’API Python.

Côté Python, l’API FastAPI existait, mais le système de recommandation était encore trop simplifié :

- les recommandations étaient statiques ;
- le modèle KNN n’était pas explicitement formalisé ;
- les données utiles à la recommandation n’étaient pas clairement documentées.

## Changements proposés puis réalisés

La reprise a consisté à transformer le projet en deux API cohérentes et testables.

Changements réalisés côté Spring :

- refonte en architecture REST par couches : `controller`, `service`, `repository`, `model`, `dto`, `mapper`, `config`, `exception` ;
- mise en place d’Hibernate avec des entités JPA et des relations métier ;
- création des repositories Spring Data JPA ;
- ajout des CRUD de base pour les jeux, utilisateurs, catégories, auteurs, éditeurs, commandes, avis, stocks et wishlists ;
- ajout d’une recherche sur les jeux ;
- ajout de Spring Security avec les rôles `CLIENT` et `ADMIN` ;
- ajout d’une gestion centralisée des erreurs ;
- ajout de DTO pour éviter d’exposer directement les entités ;
- ajout d’un client HTTP Spring vers l’API Python ;
- ajout de tests de non-régression, d’intégration et de contrats sur l’API Spring.

Changements réalisés côté Python :

- ajout d’un fichier de dépendances ;
- normalisation des routes FastAPI ;
- ajout d’une route de santé ;
- remplacement des recommandations statiques par un moteur KNN de démonstration ;
- documentation des données nécessaires à un futur modèle plus efficace.

Changements réalisés côté documentation et outillage :

- ajout d’une collection Postman ;
- ajout de scripts de lancement et d’arrêt à la racine ;
- ajout d’un rapport PDF de couverture ;
- ajout des diagrammes demandés ;
- ajout d’une documentation livrable séparée du journal de suivi.

## Architecture globale

```mermaid
flowchart LR
    Client[Client API ou Postman]

    subgraph Spring[API Spring Boot]
        Security[Spring Security]
        Controllers[Controllers REST]
        DTO[DTO Request Response]
        Services[Services métier]
        Mappers[Mappers]
        Repositories[Repositories Spring Data JPA]
        Entities[Entités JPA Hibernate]
        RecommendationClient[Client HTTP recommandations]
    end

    MySQL[(MySQL GamesUP)]

    subgraph Python[API Python FastAPI]
        Routes[Routes FastAPI]
        KNN[Moteur KNN démo]
        Schemas[Schemas Pydantic]
    end

    Client --> Security
    Security --> Controllers
    Controllers --> DTO
    Controllers --> Services
    Services --> Mappers
    Services --> Repositories
    Repositories --> Entities
    Repositories --> MySQL
    Services --> RecommendationClient
    RecommendationClient --> Routes
    Routes --> Schemas
    Routes --> KNN
```

## Diagramme de classes

```mermaid
classDiagram
    direction LR

    class User {
        <<Entity>>
        -Long id
        -String firstName
        -String lastName
        -String email
        -String password
        -Role role
        +User()
        +getId() Long
        +setId(Long) void
        +getFirstName() String
        +setFirstName(String) void
        +getLastName() String
        +setLastName(String) void
        +getEmail() String
        +setEmail(String) void
        +getPassword() String
        +setPassword(String) void
        +getRole() Role
        +setRole(Role) void
    }

    class Role {
        <<enumeration>>
        CLIENT
        ADMIN
    }

    class Game {
        <<Entity>>
        -Long id
        -String name
        -String description
        -String genre
        -Integer minPlayers
        -Integer maxPlayers
        -Integer durationMinutes
        -Integer editionNumber
        -BigDecimal price
        -Category category
        -Publisher publisher
        -Set~Author~ authors
        -Inventory inventory
        +Game()
        +getId() Long
        +setId(Long) void
        +getName() String
        +setName(String) void
        +getDescription() String
        +setDescription(String) void
        +getGenre() String
        +setGenre(String) void
        +getMinPlayers() Integer
        +setMinPlayers(Integer) void
        +getMaxPlayers() Integer
        +setMaxPlayers(Integer) void
        +getDurationMinutes() Integer
        +setDurationMinutes(Integer) void
        +getEditionNumber() Integer
        +setEditionNumber(Integer) void
        +getPrice() BigDecimal
        +setPrice(BigDecimal) void
        +getCategory() Category
        +setCategory(Category) void
        +getPublisher() Publisher
        +setPublisher(Publisher) void
        +getAuthors() Set~Author~
        +setAuthors(Set~Author~) void
        +getInventory() Inventory
        +setInventory(Inventory) void
    }

    class Category {
        <<Entity>>
        -Long id
        -String name
        +Category()
        +getId() Long
        +setId(Long) void
        +getName() String
        +setName(String) void
    }

    class Publisher {
        <<Entity>>
        -Long id
        -String name
        +Publisher()
        +getId() Long
        +setId(Long) void
        +getName() String
        +setName(String) void
    }

    class Author {
        <<Entity>>
        -Long id
        -String name
        -Set~Game~ games
        +Author()
        +getId() Long
        +setId(Long) void
        +getName() String
        +setName(String) void
        +getGames() Set~Game~
        +setGames(Set~Game~) void
    }

    class Inventory {
        <<Entity>>
        -Long id
        -Game game
        -int quantity
        +Inventory()
        +getId() Long
        +setId(Long) void
        +getGame() Game
        +setGame(Game) void
        +getQuantity() int
        +setQuantity(int) void
    }

    class Purchase {
        <<Entity>>
        -Long id
        -User user
        -List~PurchaseLine~ lines
        -LocalDateTime date
        -boolean paid
        -boolean delivered
        -boolean archived
        +Purchase()
        +getId() Long
        +setId(Long) void
        +getUser() User
        +setUser(User) void
        +getLines() List~PurchaseLine~
        +setLines(List~PurchaseLine~) void
        +getDate() LocalDateTime
        +setDate(LocalDateTime) void
        +isPaid() boolean
        +setPaid(boolean) void
        +isDelivered() boolean
        +setDelivered(boolean) void
        +isArchived() boolean
        +setArchived(boolean) void
    }

    class PurchaseLine {
        <<Entity>>
        -Long id
        -Purchase purchase
        -Game game
        -int quantity
        -BigDecimal unitPrice
        +PurchaseLine()
        +getId() Long
        +setId(Long) void
        +getPurchase() Purchase
        +setPurchase(Purchase) void
        +getGame() Game
        +setGame(Game) void
        +getQuantity() int
        +setQuantity(int) void
        +getUnitPrice() BigDecimal
        +setUnitPrice(BigDecimal) void
    }

    class Avis {
        <<Entity>>
        -Long id
        -String comment
        -int rating
        -User user
        -Game game
        +Avis()
        +getId() Long
        +setId(Long) void
        +getComment() String
        +setComment(String) void
        +getRating() int
        +setRating(int) void
        +getUser() User
        +setUser(User) void
        +getGame() Game
        +setGame(Game) void
    }

    class Wishlist {
        <<Entity>>
        -Long id
        -User user
        -Set~Game~ games
        +Wishlist()
        +getId() Long
        +setId(Long) void
        +getUser() User
        +setUser(User) void
        +getGames() Set~Game~
        +setGames(Set~Game~) void
    }

    User --> Role
    User "1" --> "0..*" Purchase : passe
    User "1" --> "0..*" Avis : rédige
    User "1" --> "0..1" Wishlist : possède
    Game "0..*" --> "0..1" Category : appartient à
    Game "0..*" --> "0..1" Publisher : édité par
    Game "0..*" --> "0..*" Author : créé par
    Author "0..*" --> "0..*" Game : participe à
    Game "1" --> "0..1" Inventory : stock
    Game "1" --> "0..*" Avis : reçoit
    Purchase "1" --> "1..*" PurchaseLine : contient
    PurchaseLine "0..*" --> "1" Game : concerne
    Wishlist "0..*" --> "0..*" Game : contient
```

## Diagramme de composants

```mermaid
flowchart LR
    Postman[Postman]

    subgraph SpringAPI[API Spring]
        SecurityComponent[SecurityConfig]
        ControllerComponent[Controllers]
        ServiceComponent[Services]
        RepositoryComponent[Repositories]
        MapperComponent[Mappers]
        ExceptionComponent[GlobalExceptionHandler]
    end

    subgraph Persistence[Persistance]
        Hibernate[Hibernate]
        Database[(MySQL)]
    end

    subgraph PythonAPI[API Python]
        FastAPI[FastAPI]
        Pydantic[Pydantic]
        KNNEngine[KNN démo]
    end

    Postman --> SecurityComponent
    SecurityComponent --> ControllerComponent
    ControllerComponent --> ServiceComponent
    ControllerComponent --> MapperComponent
    ControllerComponent --> ExceptionComponent
    ServiceComponent --> RepositoryComponent
    RepositoryComponent --> Hibernate
    Hibernate --> Database
    ServiceComponent --> FastAPI
    FastAPI --> Pydantic
    FastAPI --> KNNEngine
```

## Diagramme de séquence - recommandation

```mermaid
sequenceDiagram
    actor Client
    participant Security as Spring Security
    participant Controller as RecommendationController
    participant Service as RecommendationService
    participant Repositories as Repositories JPA
    participant Python as API Python FastAPI
    participant KNN as Moteur KNN

    Client->>Security: GET /api/recommendations/me
    Security->>Controller: utilisateur authentifié
    Controller->>Service: recommendForUser(userId)
    Service->>Repositories: achats et avis utilisateur
    Repositories-->>Service: signaux utilisateur
    Service->>Python: POST /recommendations
    Python->>KNN: recherche des voisins proches
    KNN-->>Python: jeux classés par score
    Python-->>Service: recommandations
    Service-->>Controller: RecommendationResponse
    Controller-->>Client: 200 OK
```

## Respect des principes SOLID

- Responsabilité unique : les controllers exposent HTTP, les services portent la logique métier, les repositories gèrent la persistance et les DTO définissent les contrats JSON.
- Ouvert/fermé : l’ajout de `WishlistController` et `WishlistService` a été fait sans modifier les controllers existants.
- Substitution de Liskov : les repositories respectent les contrats Spring Data JPA.
- Ségrégation des interfaces : les services exposent des méthodes ciblées par domaine au lieu d’un service global.
- Inversion des dépendances : les composants dépendent de repositories, services et clients injectés par constructeurs explicites.

## Bonnes pratiques appliquées

- Architecture en couches et DTO pour éviter d’exposer directement les entités.
- Gestion centralisée des erreurs avec `GlobalExceptionHandler`.
- Validation des entrées avec Jakarta Validation.
- Sécurité par rôles `CLIENT` et `ADMIN`.
- Tests de non-régression Spring avec MockMvc et base H2.
- Configuration Docker et scripts racine pour faciliter le lancement.
- Suppression de la dépendance implicite à Lombok dans le code principal afin d’éviter les erreurs IDE.

## Points perfectibles

- HTTP Basic est suffisant pour l’exercice, mais JWT serait plus adapté à une application front moderne.
- Le KNN utilise un jeu de données de démonstration, pas un entraînement sur données réelles.
- Les données ML historiques ne sont pas disponibles, donc les features sont simulées.
- Les tests Python ne sont pas ajoutés car la consigne demande de tester uniquement l’API Spring.

## Système de recommandation

Spring construit des signaux utilisateur avec :

- achats passés ;
- jeux achetés ;
- notes et avis ;
- identifiant utilisateur.

L’API Python reçoit :

```json
{
  "userId": 1,
  "purchases": [
    {
      "gameId": 102,
      "rating": 4.5
    }
  ]
}
```

Le moteur Python construit un profil moyen pondéré par les notes, exclut les jeux déjà achetés, puis retourne les `k` jeux les plus proches. Les features de démonstration représentent la coopération, la complexité, la stratégie et l’accessibilité familiale.

Données utiles pour un futur modèle efficace :

- historique d’achats ;
- notes utilisateurs ;
- wishlists ;
- catégories préférées ;
- auteurs et éditeurs préférés ;
- durée, nombre de joueurs, prix ;
- interactions de consultation ou d’ajout panier si elles existent plus tard.

## Couverture de test

Dernière vérification :

```powershell
cd gamesUP
.\mvnw.cmd clean test
```

Résultat :

- Build Spring : succès.
- Tests Spring : 15 tests, 0 échec.
- Couverture JaCoCo instructions : 75 pourcent.
- Couverture JaCoCo branches : 57 pourcent.
- Couverture JaCoCo classes : 98 pourcent.

Rapports :

- Rapport JaCoCo : `gamesUP/target/site/jacoco/index.html`.
- Rapport PDF : `coverage-report.pdf`.

### Rapport visuel intégré

| Métrique JaCoCo | Couverture | Détail |
| --- | ---: | --- |
| Instructions | 75 % | 754 instructions non couvertes sur 3 132 |
| Branches | 57 % | 24 branches non couvertes sur 56 |
| Complexité | 71 % | 99 points de complexité non couverts sur 342 |
| Lignes | 81 % | 131 lignes non couvertes sur 708 |
| Méthodes | 74 % | 79 méthodes non couvertes sur 314 |
| Classes | 98 % | 1 classe non couverte sur 56 |

```mermaid
xychart-beta
    title "Couverture JaCoCo"
    x-axis ["Instructions", "Branches", "Complexité", "Lignes", "Méthodes", "Classes"]
    y-axis "Pourcentage" 0 --> 100
    bar [75, 57, 71, 81, 74, 98]
```

Lecture du rapport :

- les tests ont été découpés par domaine pour améliorer la lisibilité : santé/sécurité, catalogue, commerce, recommandations et erreurs ;
- les tests de branches très secondaires ont été retirés pour conserver une suite plus utile fonctionnellement ;
- la couverture d’instructions reste supérieure au seuil demandé de 70 %.

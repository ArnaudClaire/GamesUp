# GamesUP

GamesUP est un projet composé de deux API :

- une API Spring Boot pour la gestion du catalogue, des utilisateurs, des commandes et de la sécurité ;
- une API Python FastAPI pour produire des recommandations de jeux avec un modèle KNN de démonstration.

## Documents utiles

- `DOCUMENTATION_LIVRABLE.md` : document principal de livraison avec diagrammes, architecture, SOLID, couverture et recommandation.
- `coverage-report.pdf` : rapport PDF de couverture JaCoCo.
- `GamesUP.postman_collection.json` : collection Postman pour tester les routes Spring et Python.
- `Enonce_Etude-de-cas-GamesUP.pdf` : énoncé de référence.
- `FT_Etude-de-cas-GamesUP.pdf` : fiche technique de référence.
- `suivi avec assistance IA CODEX.md` : journal de suivi et historique de travail avec assistance IA.

## Installation

### Prérequis

- Java JDK 17.
- Docker Desktop.
- Python 3.12 recommandé.
- Postman, optionnel mais conseillé pour tester rapidement les routes.

### Lancement complet avec Docker MySQL

Depuis la racine du projet :

```powershell
.\start-all.ps1
```

Ce script démarre :

- MySQL dans Docker ;
- l’API Python sur `http://localhost:8000` ;
- l’API Spring sur `http://localhost:8081`.

Arrêt complet :

```powershell
.\stop-all.ps1
```

Arrêt des API en conservant MySQL :

```powershell
.\stop-all.ps1 -KeepMySql
```

## Tests

### API Spring

```powershell
cd gamesUP
.\mvnw.cmd clean test
```

Rapport JaCoCo généré :

```text
gamesUP/target/site/jacoco/index.html
```

Rapport PDF fourni :

```text
coverage-report.pdf
```

### API Python

```powershell
python -m py_compile CodeApiPython\main.py CodeApiPython\models.py CodeApiPython\recommendation.py CodeApiPython\data_loader.py
```

## Architecture du projet

```text
ANNEXES/
├── gamesUP/                         API Spring Boot
│   ├── src/main/java/.../config      Configuration, sécurité, seed data
│   ├── src/main/java/.../controller  Controllers REST
│   ├── src/main/java/.../dto         Contrats JSON
│   ├── src/main/java/.../exception   Gestion centralisée des erreurs
│   ├── src/main/java/.../mapper      Mapping entités vers DTO
│   ├── src/main/java/.../model       Entités JPA Hibernate
│   ├── src/main/java/.../repository  Repositories Spring Data JPA
│   ├── src/main/java/.../service     Logique métier
│   └── src/test/java                 Tests Spring
├── CodeApiPython/                    API FastAPI de recommandation
├── GamesUP.postman_collection.json   Collection Postman
├── DOCUMENTATION_LIVRABLE.md         Documentation finale
├── coverage-report.pdf               Couverture de tests
├── start-all.ps1                     Lancement complet
└── stop-all.ps1                      Arrêt des services
```

## Comptes de démonstration

Créés au démarrage si les données de démonstration sont activées :

- administrateur : `admin@gamesup.local` / `admin123`
- client : `client@gamesup.local` / `client123`

## Routes principales

Routes publiques :

- `GET /api/health`
- `GET /api/games`
- `GET /api/games?search=...`
- `GET /api/categories`
- `GET /api/authors`
- `GET /api/publishers`
- `POST /api/users`

Routes client ou administrateur :

- `GET /api/users/me`
- `GET /api/reviews`
- `POST /api/reviews`
- `POST /api/recommendations`
- `GET /api/recommendations/me`

Routes administrateur :

- CRUD jeux, utilisateurs, catégories, auteurs, éditeurs, commandes, stocks, avis et wishlists.

## Tester avec Postman

Importer :

```text
GamesUP.postman_collection.json
```

Variables importantes :

- `springBaseUrl = http://localhost:8081`
- `pythonBaseUrl = http://localhost:8000`
- `adminEmail = admin@gamesup.local`
- `adminPassword = admin123`
- `clientEmail = client@gamesup.local`
- `clientPassword = client123`

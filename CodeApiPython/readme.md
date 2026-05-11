# API Python GamesUP

API FastAPI de démonstration pour les recommandations GamesUP.

## Installation

```bash
python -m venv .venv
.venv\Scripts\activate
python -m pip install -r requirements.txt
```

## Lancement

```bash
uvicorn main:app --reload --port 8000
```

## Routes

- `GET /` : message de disponibilité.
- `GET /health` : statut technique.
- `POST /recommendations` : recommandations KNN de démonstration.

## Données utilisées par le KNN

Le modèle de démonstration utilise actuellement des vecteurs internes par jeu :

- orientation coopérative ;
- complexité ;
- dimension stratégie ;
- accessibilité familiale.

Les données utilisateur envoyées par Spring sont :

- `userId` : identifiant utilisateur ;
- `purchases` : liste des jeux achetés ou notés ;
- `gameId` : identifiant du jeu ;
- `rating` : signal d’appréciation entre 0 et 5.

Le moteur construit un profil utilisateur moyen pondéré par les notes, retire les jeux déjà achetés, puis retourne les `k` jeux les plus proches selon une similarité cosinus. Le jeu de données est volontairement petit car les données réelles ne sont pas encore disponibles.

Exemple :

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

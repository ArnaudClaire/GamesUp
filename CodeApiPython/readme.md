# API Python GamesUP

API FastAPI de demonstration pour les recommandations GamesUP.

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

- `GET /` : message de disponibilite.
- `GET /health` : statut technique.
- `POST /recommendations` : recommandations KNN de demonstration.

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

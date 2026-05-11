from fastapi import FastAPI, HTTPException

from models import Recommendation, UserData
from recommendation import generate_recommendations

app = FastAPI(title="GamesUP Recommendation API", version="1.0.0")


@app.get("/")
async def root():
    return {"message": "API de recommandation GamesUP en ligne"}


@app.get("/health")
async def health():
    return {"status": "UP", "application": "gamesup-recommendation-api"}


@app.post("/recommendations", response_model=list[Recommendation])
async def get_recommendations(data: UserData):
    try:
        return generate_recommendations(data)
    except Exception as exception:
        raise HTTPException(status_code=500, detail=str(exception)) from exception

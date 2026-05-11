from typing import List

from pydantic import BaseModel, Field


class UserPurchase(BaseModel):
    game_id: int = Field(alias="gameId")
    rating: float

    class Config:
        populate_by_name = True


class UserData(BaseModel):
    user_id: int = Field(alias="userId")
    purchases: List[UserPurchase]

    class Config:
        populate_by_name = True


class Recommendation(BaseModel):
    game_id: int = Field(alias="gameId")
    game_name: str = Field(alias="gameName")
    score: float
    reason: str

    class Config:
        populate_by_name = True

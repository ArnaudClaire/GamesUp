from models import UserData


GAME_FEATURES = {
    101: {"name": "Pandemic", "features": [0.9, 0.8, 0.2, 0.6]},
    102: {"name": "Catan", "features": [0.8, 0.3, 0.9, 0.7]},
    103: {"name": "Ticket to Ride", "features": [0.5, 0.2, 0.7, 0.9]},
    104: {"name": "7 Wonders", "features": [0.7, 0.2, 0.8, 0.6]},
    105: {"name": "Carcassonne", "features": [0.4, 0.3, 0.7, 0.8]},
    106: {"name": "Azul", "features": [0.3, 0.1, 0.5, 0.9]},
}


def generate_recommendations(user_data: UserData, limit: int = 3):
    purchased_ids = {purchase.game_id for purchase in user_data.purchases}
    profile = build_user_profile(user_data)

    scored_games = []
    for game_id, game in GAME_FEATURES.items():
        if game_id in purchased_ids:
            continue

        score = cosine_similarity(profile, game["features"])
        scored_games.append(
            {
                "gameId": game_id,
                "gameName": game["name"],
                "score": round(score, 4),
                "reason": "KNN demo: similarite avec les achats et notes utilisateur",
            }
        )

    return sorted(scored_games, key=lambda item: item["score"], reverse=True)[:limit]


def build_user_profile(user_data: UserData):
    if not user_data.purchases:
        return average_vector([game["features"] for game in GAME_FEATURES.values()])

    weighted_vectors = []
    for purchase in user_data.purchases:
        game = GAME_FEATURES.get(purchase.game_id)
        if game is None:
            continue
        normalized_rating = max(purchase.rating, 0.1) / 5
        weighted_vectors.append([value * normalized_rating for value in game["features"]])

    if not weighted_vectors:
        return average_vector([game["features"] for game in GAME_FEATURES.values()])

    return average_vector(weighted_vectors)


def average_vector(vectors):
    size = len(vectors[0])
    return [sum(vector[index] for vector in vectors) / len(vectors) for index in range(size)]


def cosine_similarity(left, right):
    dot = sum(a * b for a, b in zip(left, right))
    left_norm = sum(a * a for a in left) ** 0.5
    right_norm = sum(b * b for b in right) ** 0.5
    if left_norm == 0 or right_norm == 0:
        return 0
    return dot / (left_norm * right_norm)

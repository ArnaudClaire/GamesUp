from models import UserData


K_NEIGHBORS = 3

GAME_FEATURES = {
    101: {"name": "Pandemic", "features": [0.9, 0.8, 0.2, 0.6]},
    102: {"name": "Catan", "features": [0.8, 0.3, 0.9, 0.7]},
    103: {"name": "Ticket to Ride", "features": [0.5, 0.2, 0.7, 0.9]},
    104: {"name": "7 Wonders", "features": [0.7, 0.2, 0.8, 0.6]},
    105: {"name": "Carcassonne", "features": [0.4, 0.3, 0.7, 0.8]},
    106: {"name": "Azul", "features": [0.3, 0.1, 0.5, 0.9]},
}


def generate_recommendations(user_data: UserData, limit: int = 3):
    """Generate recommendations with a demo KNN approach."""
    purchased_ids = {purchase.game_id for purchase in user_data.purchases}
    profile = build_user_profile(user_data)
    neighbors = find_nearest_neighbors(profile, purchased_ids, K_NEIGHBORS)

    return [
        {
            "gameId": game_id,
            "gameName": game["name"],
            "score": round(score, 4),
            "reason": f"KNN demo: jeu parmi les {K_NEIGHBORS} voisins les plus proches",
        }
        for game_id, game, score in neighbors[:limit]
    ]


def find_nearest_neighbors(profile, excluded_ids, k_neighbors):
    """Return the nearest non-purchased games for a user profile vector."""
    scored_games = []
    for game_id, game in GAME_FEATURES.items():
        if game_id in excluded_ids:
            continue
        score = cosine_similarity(profile, game["features"])
        scored_games.append((game_id, game, score))

    return sorted(scored_games, key=lambda item: item[2], reverse=True)[:k_neighbors]


def build_user_profile(user_data: UserData):
    """Build a user vector from purchases and ratings."""
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
    """Compute the average of multiple numeric vectors."""
    size = len(vectors[0])
    return [sum(vector[index] for vector in vectors) / len(vectors) for index in range(size)]


def cosine_similarity(left, right):
    """Compute cosine similarity between two vectors."""
    dot = sum(a * b for a, b in zip(left, right))
    left_norm = sum(a * a for a in left) ** 0.5
    right_norm = sum(b * b for b in right) ** 0.5
    if left_norm == 0 or right_norm == 0:
        return 0
    return dot / (left_norm * right_norm)

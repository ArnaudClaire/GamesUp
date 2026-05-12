package com.gamesUP.gamesUP.exception;

/**
 * Exception levée lorsqu'une requête entre en conflit avec des données existantes.
 */
public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}

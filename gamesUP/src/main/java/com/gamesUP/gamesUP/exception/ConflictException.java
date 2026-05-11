package com.gamesUP.gamesUP.exception;

/**
 * Exception thrown when a request conflicts with existing data.
 */
public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}

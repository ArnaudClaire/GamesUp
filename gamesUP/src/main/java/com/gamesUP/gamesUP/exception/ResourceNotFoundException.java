package com.gamesUP.gamesUP.exception;

/**
 * Exception levée lorsqu'une ressource demandée est introuvable.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceName, Long id) {
        super(resourceName + " not found with id " + id);
    }
}

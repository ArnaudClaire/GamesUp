package com.gamesUP.gamesUP.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * Tests unitaires du contrat minimal des exceptions métier.
 */
class ExceptionContractTests {

    /**
     * Vérifie que l'exception de ressource absente formate le nom de ressource et son identifiant.
     *
     * @throws Exception si la construction réflexive de l'exception échoue.
     */
    @Test
    void resourceNotFoundExceptionFormateLeNomDeRessourceEtSonId() throws Exception {
        Object exception = exception("ResourceNotFoundException")
                .getConstructor(String.class, Long.class)
                .newInstance("Game", 42L);

        assertThat(((RuntimeException) exception).getMessage()).isEqualTo("Game not found with id 42");
    }

    /**
     * Vérifie que l'exception de conflit conserve le message métier reçu.
     *
     * @throws Exception si la construction réflexive de l'exception échoue.
     */
    @Test
    void conflictExceptionConserveLeMessageMetier() throws Exception {
        Object exception = exception("ConflictException")
                .getConstructor(String.class)
                .newInstance("Email already exists");

        assertThat(((RuntimeException) exception).getMessage()).isEqualTo("Email already exists");
    }

    /**
     * Charge une classe d'exception par son nom simple.
     *
     * @param simpleName nom simple de l'exception.
     * @return classe Java correspondante.
     * @throws Exception si la classe est introuvable.
     */
    private Class<?> exception(String simpleName) throws Exception {
        return Class.forName("com.gamesUP.gamesUP.exception." + simpleName);
    }
}

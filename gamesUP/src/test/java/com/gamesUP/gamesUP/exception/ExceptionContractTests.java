package com.gamesUP.gamesUP.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ExceptionContractTests {

    @Test
    void resourceNotFoundExceptionFormateLeNomDeRessourceEtSonId() throws Exception {
        Object exception = exception("ResourceNotFoundException")
                .getConstructor(String.class, Long.class)
                .newInstance("Game", 42L);

        assertThat(((RuntimeException) exception).getMessage()).isEqualTo("Game not found with id 42");
    }

    @Test
    void conflictExceptionConserveLeMessageMetier() throws Exception {
        Object exception = exception("ConflictException")
                .getConstructor(String.class)
                .newInstance("Email already exists");

        assertThat(((RuntimeException) exception).getMessage()).isEqualTo("Email already exists");
    }

    private Class<?> exception(String simpleName) throws Exception {
        return Class.forName("com.gamesUP.gamesUP.exception." + simpleName);
    }
}

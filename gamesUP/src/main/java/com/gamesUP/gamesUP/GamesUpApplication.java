package com.gamesUP.gamesUP;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
/**
 * Démarre l'API Spring Boot GamesUP.
 */
public class GamesUpApplication {

	/**
	 * Point d'entrée de l'application.
	 *
	 * @param args arguments de ligne de commande
	 */
	public static void main(String[] args) {
		SpringApplication.run(GamesUpApplication.class, args);
	}

}

package com.projet5.safetyNet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe principale de l'application SafetyNet. Cette application sert de point
 * d'entrée pour le framework Spring Boot.
 */
@SpringBootApplication
public class SafetyNetApplication {

	/**
	 * Instance de logger pour la classe SafetyNetApplication.
	 */
	private static final Logger logger = LoggerFactory.getLogger(SafetyNetApplication.class);

	/**
	 * Méthode principale pour lancer l'application SafetyNet.
	 * 
	 * @param args Arguments de ligne de commande passés à l'application.
	 */
	public static void main(String[] args) {
		logger.debug("Démarrage de l'application SafetyNet...");
		SpringApplication.run(SafetyNetApplication.class, args);
		logger.info("L'application SafetyNet a démarré avec succès.");

	}
}

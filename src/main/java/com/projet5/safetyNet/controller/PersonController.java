package com.projet5.safetyNet.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.projet5.safetyNet.model.Person;
import com.projet5.safetyNet.service.PersonService;

import java.util.List;

@RestController
@RequestMapping("/persons")
public class PersonController {

	private static Logger logger = LogManager.getLogger(PersonController.class);
	private final PersonService personService;

	public PersonController(PersonService personService) {
		this.personService = personService;
	}

	/**
	 * Récupérer toutes les personnes.
	 * 
	 * @return Liste des personnes ou un message d'erreur en cas de problème.
	 */
	@GetMapping
	public ResponseEntity<?> getAllPersons() {
		logger.info("Entrée dans la méthode getAllPersons() de la classe PersonController.");
		try {
			logger.info("Appel de la méthode personService.getAllPersons()");
			List<Person> persons = personService.getAllPersons();
			logger.info("Récupération des personnes réussie.");
			return ResponseEntity.ok(persons);
		} catch (Exception e) {
			logger.error("Erreur lors de la récupération des personnes.", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erreur lors de la récupération des personnes : " + e.getMessage());
		}
	}

	/**
	 * Ajouter une nouvelle personne.
	 * 
	 * @param person est un objet `Person` reçu en JSON dans le corps de la requête.
	 * @return Message de confirmation ou un message d'erreur.
	 * @throws Exception
	 */
	@PostMapping
	public ResponseEntity<?> addPerson(@RequestBody Person person) throws Exception {
		logger.info("Entrée dans la méthode addPerson() de la classe PersonController.");
		try {
			logger.info("Appel de la méthode personService.addPerson()");
			personService.addPerson(person);
			logger.info("Ajout de la nouvelle personne réussi.");
			return ResponseEntity.ok("Personne ajoutée avec succès !");
		} catch (Exception e) {
			logger.error("Erreur lors de la récupération des personnes.", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erreur lors de l'ajout d'une nouvelle personne : " + e.getMessage());
		}

	}

	/**
	 * Mettre à jour les informations d'une personne.
	 * 
	 * @param person est un objet `Person` reçu en JSON dans le corps de la requête.
	 * @return Message de confirmation ou un message d'erreur.
	 */
	@PutMapping
	public ResponseEntity<?> updatePerson(@RequestBody Person person) {
		logger.info("Entrée dans la méthode updatePerson() de la classe PersonController.");
		try {
			logger.info("Appel de la méthode personService.updatePerson()");
			personService.updatePerson(person);
			logger.info("Mise à jour de la personne réussi.");
			return ResponseEntity.ok("Personne mise à jour avec succès !");
		} catch (Exception e) {
			logger.error("Erreur lors de la mise à jour de la personne : " + person, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erreur lors de la mise à jour de la personne." + e.getMessage());
		}

	}

	/**
	 * Supprimer une personne.
	 * 
	 * @param person est un objet `Person` reçu en JSON dans le corps de la requête.
	 * @return Message de confirmation ou un message d'erreur.
	 * @throws Exception
	 */
	@DeleteMapping
	public ResponseEntity<?> deletePerson(@RequestBody Person person) {
		logger.info("Entrée dans la méthode deletePerson() de la classe PersonController.");
		try {
			logger.info("Appel de la méthode personService.deletePerson()");
			personService.deletePerson(person.getFirstName(), person.getLastName(), person.getPhone());
			logger.info("Suppression de la nouvelle personne réussi.");
			return ResponseEntity.ok("Personne supprimée avec succès.");
		} catch (Exception e) {
			logger.error("Erreur lors de la suppression de la personne : " + person, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erreur lors de la suppression de la personne." + e.getMessage());
		}
	}
}

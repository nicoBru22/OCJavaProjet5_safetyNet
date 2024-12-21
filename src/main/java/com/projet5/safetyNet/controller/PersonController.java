package com.projet5.safetyNet.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.projet5.safetyNet.model.Person;
import com.projet5.safetyNet.service.PersonService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
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
	@GetMapping("/persons")
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
	@PostMapping("/persons")
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
	@DeleteMapping("/persons")
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

	/**
	 * Récupère la liste des adresses email des personnes selon leur ville.
	 * 
	 * <p>
	 * Cette méthode permet de récupérer la liste des adresses email des personnes
	 * qui résident dans la ville spécifiée en paramètre. Elle appelle le service
	 * pour récupérer les données correspondantes et retourne une liste contenant
	 * les adresses email des personnes habitant dans la ville donnée.
	 * </p>
	 * 
	 * @param city le nom de la ville dans laquelle rechercher les adresses email
	 *             des personnes.
	 * @return une liste contenant les adresses email des personnes vivant dans la
	 *         ville spécifiée.
	 * @throws Exception si une erreur survient lors de la récupération des données
	 *                   (par exemple, si la ville n'est pas trouvée ou si une
	 *                   erreur technique survient).
	 */
	@GetMapping("/communityEmail")
	public List<String> getCommunityEmail(@RequestParam String city) throws Exception {
		logger.info(
				"Entrée dans la méthode getCommunityEmail() de la classe PersonController, recherche pour la ville : {}",
				city);
		try {
			List<String> communityEmail = personService.getCommunityEmail(city);
			return communityEmail;
		} catch (Exception e) {
			logger.error("Une erreur s'est produite lors de la récupération des adresses email pour la ville : {}",
					city, e);
			throw new Exception(
					"Une erreur s'est produite lors de la récupération des adresses email pour la ville : " + city, e);
		}
	}

	/**
	 * 
	 * @param address
	 * @return une liste contenant les enfants vivant à l'adresse donnée.
	 * @throws Exception si une erreur survient lors de la récupération des données
	 *                   (par exemple, si la ville n'est pas trouvée ou si une
	 *                   erreur technique survient).
	 */
	@GetMapping("/childAlert")
	public List<String> getChildListFromAddress(@RequestParam String address) throws Exception {
		List<String> childList = personService.getChildListFromAddress(address);
		return childList;
	}

	/**
	 * 
	 * @param lastName le nom des personne dont on veut les informations.
	 * @return une liste d'information sur les personnes dont le nom est passé en paramètre. 
	 * @throws Exception si une erreur survient lors de la récupération des données
	 *                   (par exemple, si la ville n'est pas trouvée ou si une
	 *                   erreur technique survient).
	 */
	@GetMapping("/personInfolastName")
	public Map<String, Object> getPersonInfoLastName(@RequestParam String lastName) throws Exception {
		logger.info(
				"Entrée dans la méthode getPersonInfoLastName() de la classe PersonController, recherche par nom de famille : {}",
				lastName);
		try {
			Map<String, Object> personInfo = personService.personInfo(lastName);
			return personInfo;
		} catch (Exception e) {
			throw new Exception(
					"Une erreur est survenue dans la récupération de la liste d'information des personnes selon le nom.",
					e);
		}

	}
}

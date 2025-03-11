package com.projet5.safetyNet.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.projet5.safetyNet.model.Person;
import com.projet5.safetyNet.service.PersonService;

import java.util.List;
import java.util.Map;

/**
 * Contrôleur REST pour la gestion des personnes. *
 * 
 * Ce contrôleur fournit des points d'entrée pour :
 * <ul>
 * <li>Récupérer toutes les personnes.</li>
 * <li>Ajouter une nouvelle personne.</li>
 * <li>Mettre à jour les données d'une personne.</li>
 * <li>Supprimer les données d'une personne.</li>
 * <li>Récupérer les toutes les adresses mails des personnes dans une ville
 * donée.</li>
 * <li>Récupérer la liste des personnes avec leurs dossiers médicaux selon leur
 * nom de famille.</li>
 * <li>Récupérer une liste d'enfant à une adresse donnée.</li>
 * </ul>
 * Il fait appel au {@link PersonService} pour effectuer les opérations métier.
 */
@RestController
public class PersonController {

	/**
	 * Logger pour enregistrer les informations et les erreurs relatives à
	 * {@link PersonController}.
	 */
	private static final Logger logger = LogManager.getLogger(PersonController.class);

	/**
	 * Service pour effectuer les opérations liées aux personnes.
	 */
	private final PersonService personService;

	/**
	 * Constructeur de la classe {@link PersonController}.
	 * 
	 * @param personService le service utilisé pour gérer les opérations sur les
	 *                      personnes
	 */
	public PersonController(PersonService personService) {
		this.personService = personService;
	}

	/**
	 * Récupère la liste de toutes les personnes dans la base de données.
	 *
	 * Cette méthode appelle le service `personService.getAllPersons()` pour récupérer toutes les
	 * personnes stockées dans la base de données. Elle renvoie ensuite la liste des personnes dans
	 * une réponse HTTP.
	 *
	 * @return Une réponse HTTP contenant la liste de toutes les personnes, avec un code de statut HTTP 200.
	 */
	@GetMapping("/persons")
	public ResponseEntity<Object> getAllPersons() {
		logger.info("Entrée dans la méthode getAllPersons() de la classe PersonController.");
		logger.info("Appel de la méthode personService.getAllPersons()");
		List<Person> persons = personService.getAllPersons();
		logger.debug("Récupération avec succès de la liste de personne : ", persons);
		return ResponseEntity.ok(persons);
	}

	/**
	 * Ajoute une nouvelle personne dans la base de données.
	 *
	 * Cette méthode permet d'ajouter une nouvelle personne en utilisant les données fournies dans 
	 * l'objet `Person` transmis dans la requête. Elle appelle le service `personService.addPerson()`
	 * pour effectuer l'ajout dans la base de données.
	 *
	 * @param person L'objet `Person` contenant les informations de la nouvelle personne à ajouter.
	 * @return Une réponse HTTP avec un message indiquant que la personne a été ajoutée avec succès.
	 */
	@PostMapping("/persons")
	public ResponseEntity<String> addPerson(@RequestBody Person person) {
		logger.info("Entrée dans la méthode addPerson() de la classe PersonController.");
		logger.info("Appel de la méthode personService.addPerson()");
		personService.addPerson(person);
		return ResponseEntity.ok("Personne ajoutée avec succès !");
	}

	/**
	 * Met à jour les informations d'une personne dans la base de données.
	 *
	 * Cette méthode permet de mettre à jour les informations d'une personne existante en utilisant
	 * les données fournies dans l'objet `Person` transmis dans la requête. Elle appelle le service 
	 * `personService.updatePerson()` pour effectuer la mise à jour dans la base de données.
	 *
	 * @param person L'objet `Person` contenant les informations mises à jour de la personne.
	 * @return Une réponse HTTP avec un message indiquant que la mise à jour a été effectuée avec succès.
	 */
	@PutMapping("/persons")
	public ResponseEntity<String> updatePerson(@RequestBody Person person) {
		logger.info("Entrée dans la méthode updatePerson() de la classe PersonController.");
		logger.info("Appel de la méthode personService.updatePerson()");
		personService.updatePerson(person);
		return ResponseEntity.ok("Personne mise à jour avec succès !");

	}

	/**
	 * Supprime une personne en fonction de ses informations personnelles.
	 * 
	 * Cette méthode permet de supprimer une personne de la base de données en utilisant les
	 * informations fournies dans l'objet `Person` transmis dans la requête. La méthode fait appel
	 * au service pour effectuer la suppression de la personne.
	 *
	 * @param person L'objet `Person` contenant les informations (prénom, nom, téléphone) de la personne à supprimer.
	 * @return Une réponse HTTP avec un message indiquant que la suppression a été effectuée avec succès.
	 */
	@DeleteMapping("/persons")
	public ResponseEntity<String> deletePerson(@RequestBody Person person) {
		logger.info("Entrée dans la méthode deletePerson() de la classe PersonController.");
		logger.info("Appel de la méthode personService.deletePerson()");
		personService.deletePerson(person.getFirstName(), person.getLastName(), person.getPhone());
		logger.info("Suppression de la nouvelle personne réussi.");
		logger.debug("Suppression avec succès de la personne : {}", person);
		return ResponseEntity.ok("Personne supprimée avec succès.");
	}

	/**
	 * Récupère la liste des emails de la communauté associés à une ville donnée.
	 * 
	 * Cette méthode permet de récupérer les emails de la communauté pour une ville spécifique,
	 * en appelant le service correspondant. Le nom de la ville est fourni en tant que paramètre de requête.
	 *
	 * @param city La ville pour laquelle la liste des emails de la communauté est demandée.
	 * @return Une réponse HTTP contenant la liste des emails associés à la ville donnée.
	 */
	@GetMapping("/communityEmail")
	public ResponseEntity<Object> getCommunityEmail(@RequestParam String city){
		logger.info(
				"Entrée dans la méthode getCommunityEmail() de la classe PersonController, recherche pour la ville : {}",
				city);
		logger.info("Appel de la méthode personService.getCommunityEmail(city)");
		List<String> communityEmail = personService.getCommunityEmail(city);
		return ResponseEntity.ok(communityEmail);
	}

	/**
	 * Récupère la liste des enfants associés à une adresse donnée.
	 * 
	 * Cette méthode permet de récupérer une liste d'enfants qui habitent à une adresse spécifique,
	 * en appelant le service correspondant. L'adresse est fournie en tant que paramètre de requête.
	 *
	 * @param address L'adresse pour laquelle la liste des enfants est demandée.
	 * @return Une réponse HTTP contenant la liste des enfants associés à l'adresse donnée.
	 */
	@GetMapping("/childAlert")
	public ResponseEntity<?> getChildListFromAddress(@RequestParam String address) {
		logger.info("Appel de la méthode personService.getChildListFromAddress(address).");
		List<String> childList = personService.getChildListFromAddress(address);
		logger.info("La liste d'enfant a été récupérée avec succès.");
		logger.debug("Récupération avec succès de la liste d'enfant : {}", childList);
		return ResponseEntity.ok(childList);
	}

	/**
	 * Récupère les informations d'une personne en fonction de son nom de famille.
	 * 
	 * Cette méthode permet de rechercher les informations d'une personne en se basant sur son nom de famille.
	 * Les informations récupérées sont renvoyées sous forme de carte clé-valeur.
	 *
	 * @param lastName Le nom de famille de la personne recherchée.
	 * @return Une réponse HTTP contenant les informations de la personne, sous forme d'un map.
	 * @throws Exception Si une erreur se produit lors de la récupération des informations de la personne.
	 */
	@GetMapping("/personInfolastName")
	public ResponseEntity<Object> getPersonInfoLastName(@RequestParam String lastName) throws Exception {
		logger.info(
				"Entrée dans la méthode getPersonInfoLastName() de la classe PersonController, recherche par nom de famille : {}",
				lastName);
		logger.info("Appel de la méthode personInfo = personService.personInfo(lastName)");
		Map<String, Object> personInfo = personService.personInfo(lastName);
		logger.info("La liste d'information a été récupérée avec succès.");
		logger.debug("La liste d'nformation a été récupéré et contient : ", personInfo);
		return ResponseEntity.ok(personInfo);
	}
}

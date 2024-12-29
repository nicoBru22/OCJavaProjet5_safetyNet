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
	 * Récupère la liste de toutes les personnes.
	 * 
	 * <p>
	 * Cette méthode récupère la liste de toutes les personnes existantes dans le
	 * systeme à partir de l'endpoint /person.
	 * </p>
	 * 
	 * @return ResponseEntity contenant la liste des personnes en cas de succès ou
	 *         un message d'erreur en cas d'échec.
	 */
	@GetMapping("/persons")
	public ResponseEntity<?> getAllPersons() {
		logger.info("Entrée dans la méthode getAllPersons() de la classe PersonController.");
		try {
			logger.info("Appel de la méthode personService.getAllPersons()");
			List<Person> persons = personService.getAllPersons();
			logger.info("Récupération des personnes réussie.");
			logger.debug("Récupération avec succès de la liste de personne : ", persons);
			return ResponseEntity.ok(persons);
		} catch (Exception e) {
			logger.error("Erreur lors de la récupération des personnes.", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erreur lors de la récupération des personnes.");
		}
	}

	/**
	 * Ajoute une nouvelle personne.
	 * 
	 * <p>
	 * Cette méthode permet d'ajouter une nouvelle personne dans le systeme à partir
	 * de l'endpoint /person.
	 * </p>
	 * 
	 * @param person est un objet `Person` reçu en JSON dans le corps de la requête.
	 * @return ResponseEntity contenant un message de réussite en cas de succès ou
	 *         un message d'erreur en cas d'echec.
	 * @throws Exception si une erreur se produit lors de l'ajout de la personne.
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
					.body("Erreur lors de l'ajout d'une nouvelle personne");
		}

	}

	/**
	 * Met à jour les informations d'une personne.
	 * 
	 * <p>
	 * Cette méthode permet de mettre à jour les données d'une personne existante à
	 * partir des données passées dans le corps de la requête à partir de l'endpoint
	 * /persons.
	 * </p>
	 * 
	 * 
	 * @param person est un objet `Person` reçu en JSON dans le corps de la requête.
	 * @return ResponseEntity contenant un message de réussite en cas de succès ou
	 *         un message d'erreur en cas d'echec.
	 */
	@PutMapping("/persons")
	public ResponseEntity<?> updatePerson(@RequestBody Person person) {
		logger.info("Entrée dans la méthode updatePerson() de la classe PersonController.");
		try {
			logger.info("Appel de la méthode personService.updatePerson()");
			personService.updatePerson(person);
			logger.info("Mise à jour de la personne réussi.");
			logger.debug("Mise à jour avec succès de la personne : ", person);
			return ResponseEntity.ok("Personne mise à jour avec succès !");
		} catch (Exception e) {
			logger.error("Erreur lors de la mise à jour de la personne : " + person, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erreur lors de la mise à jour de la personne.");
		}

	}

	/**
	 * Supprime une personne.
	 * 
	 * <p>
	 * Cette méthode permet de supprimer une personne à partir des informations
	 * contenu dans la requête et dans le systeme à l'endpoint /persons.
	 * </p>
	 * 
	 * @param person est un objet `Person` reçu en JSON dans le corps de la requête.
	 * @return ResponseEntity contenant un message de réussite en cas de succès ou
	 *         un message d'erreur en cas d'echec.
	 */
	@DeleteMapping("/persons")
	public ResponseEntity<?> deletePerson(@RequestBody Person person) {
		logger.info("Entrée dans la méthode deletePerson() de la classe PersonController.");
		try {
			logger.info("Appel de la méthode personService.deletePerson()");
			personService.deletePerson(person.getFirstName(), person.getLastName(), person.getPhone());
			logger.info("Suppression de la nouvelle personne réussi.");
			logger.debug("Suppression avec succès de la personne : {}", person);
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
	 * @return ResponseEntity contenant une liste d'adresses email des personnes
	 *         vivant dans la ville spécifiée en cas de succès ou un message
	 *         d'erreur en cas d'échec.
	 * @throws Exception si une erreur se produit lors de la récupération des
	 *                   adresses mails.
	 */
	@GetMapping("/communityEmail")
	public ResponseEntity<?> getCommunityEmail(@RequestParam String city) throws Exception {
		logger.info(
				"Entrée dans la méthode getCommunityEmail() de la classe PersonController, recherche pour la ville : {}",
				city);
		try {
			logger.info("APpel de la méthode personService.getCommunityEmail(city)");
			List<String> communityEmail = personService.getCommunityEmail(city);
			logger.info("La liste des adresses mails a été récupérée avec succès.");
			logger.debug("La liste des adresse mails récupérée avec succès : {}", communityEmail);
			return ResponseEntity.ok(communityEmail);
		} catch (Exception e) {
			logger.error("Une erreur s'est produite lors de la récupération des adresses email pour la ville : {}",
					city, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Une erreur s'est produite lors de la récupération des adresses mails.");
		}
	}

	/**
	 * Récupère une liste d'enfant.
	 * 
	 * <p>
	 * Cette méthode permet de récupérer une liste d'enfants habitant à l'adresse
	 * passée en paramètre à partir de l'endpoint /childAlert.
	 * </p>
	 * 
	 * @param address l'adresse où vivent un ou des enfants.
	 * @return ResponseEntity contenant une liste d'enfant vivant à l'adresse donnée
	 *         en cas de succès ou un message d'erreur en cas d'echec.
	 * @throws Exception si une erreur se produit lors de la récupération de la
	 *                   liste d'enfant.
	 */
	@GetMapping("/childAlert")
	public ResponseEntity<?> getChildListFromAddress(@RequestParam String address) throws Exception {
		try {
			logger.info("Appel de la méthode personService.getChildListFromAddress(address).");
			List<String> childList = personService.getChildListFromAddress(address);
			logger.info("La liste d'enfant a été récupérée avec succès.");
			logger.debug("Récupération avec succès de la liste d'enfant : {}", childList);
			return ResponseEntity.ok(childList);
		} catch (Exception e) {
			logger.error("Erreur lors de la récupération de la liste d'enfant à l'adresse {}", address, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erreur lors de la récupération de la liste d'enfant.");
		}

	}

	/**
	 * Récupère les informations médical des personnes selon leur nom.
	 * 
	 * <p>
	 * Cette méthode permet de récupérer les informations médicales concernant les
	 * personnes dont le nom est donnée en paramètre à partir de l'endpoint
	 * /personInfoLastName.
	 * </p>
	 * 
	 * @param lastName le nom des personne dont on veut les informations.
	 * @return ResponseEntity conteannt une liste d'information sur les personnes
	 *         dont le nom est passé en paramètre en cas de succès ou un message
	 *         d'erreur en cas d'echec.
	 * @throws Exception si une erreur survient lors de la récupération des données.
	 */
	@GetMapping("/personInfolastName")
	public ResponseEntity<?> getPersonInfoLastName(@RequestParam String lastName) throws Exception {
		logger.info(
				"Entrée dans la méthode getPersonInfoLastName() de la classe PersonController, recherche par nom de famille : {}",
				lastName);
		try {
			logger.info("Appel de la méthode personInfo = personService.personInfo(lastName)");
			Map<String, Object> personInfo = personService.personInfo(lastName);
			logger.info("La liste d'information a été récupérée avec succès.");
			logger.debug("La liste d'nformation a été récupéré et contient : ", personInfo);
			return ResponseEntity.ok(personInfo);
		} catch (Exception e) {
			logger.error(
					"Une erreur est survenue dans la récupération de la liste d'information des personnes dont le nom est : .",
					lastName, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Une erreur est survenue dans la récupération de la liste d'information des personnes");
		}

	}
}

package com.projet5.safetyNet.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.projet5.safetyNet.model.Firestation;
import com.projet5.safetyNet.service.FirestationService;

/**
 * Contrôleur REST pour la gestion des casernes de pompiers.
 * 
 * Ce contrôleur fournit des points d'entrée pour :
 * <ul>
 * <li>Récupérer toutes les casernes de pompiers.</li>
 * <li>Ajouter une nouvelle caserne.</li>
 * <li>Mettre à jour une caserne existante.</li>
 * <li>Supprimer une caserne.</li>
 * <li>Récupérer les informations des personnes associées à une caserne
 * spécifique.</li>
 * </ul>
 * Il fait appel au {@link FirestationService} pour effectuer les opérations
 * métier.
 */
@RestController
public class FirestationController {

	private static final Logger logger = LoggerFactory.getLogger(FirestationController.class);

	/**
	 * Service de gestion des casernes de pompiers.
	 */
	private final FirestationService firestationService;

	/**
	 * Constructeur du contrôleur injectant le service {@link FirestationService}.
	 * 
	 * @param firestationService le service pour la gestion des casernes
	 */
	public FirestationController(FirestationService firestationService) {
		this.firestationService = firestationService;
	}

	/**
	 * Récupère la liste complète des casernes de pompiers.
	 * 
	 * Cette méthode retourne une liste de toutes les casernes de pompiers
	 * existantes dans le système à partir du endpoint /firestation.
	 * 
	 * @return ResponseEntity contenant la liste des casernes de pompiers en cas de
	 *         succès ou un message d'erreur en cas de problème.
	 */
	@GetMapping("/firestations")
	public ResponseEntity<List<Firestation>> getAllFireStation() {
			logger.info("Récupération de toutes les casernes de pompiers.");
			List<Firestation> firestations = firestationService.getAllFireStations();
			return ResponseEntity.ok(firestations);
	}

	/**
	 * Ajoute une nouvelle caserne de pompiers.
	 * <p>
	 * Cette méthode permet d'ajouter une nouvelle caserne dans le système à partir
	 * de l'endpoint /firestation.
	 * </p>
	 * 
	 * @param newFirestation la caserne à ajouter
	 * @return ResponseEntity contenant un message de succès en cas de réussite ou
	 *         un message d'erreur en cas d'échec.
	 * 
	 */
	@PostMapping("/firestation")
	public ResponseEntity<String> addFirestation(@RequestBody Firestation newFirestation) {
			logger.info("Ajout d'une nouvelle caserne : {}", newFirestation);
			firestationService.addFirestation(newFirestation);
			logger.info("La caserne a été ajoutée avec succès.");
			logger.debug("Caserne {} ajoutée avec succès.", newFirestation);
			return ResponseEntity.status(HttpStatus.CREATED).body("La caserne a été ajoutée avec succès !");
	}

	/**
	 * Supprime une caserne de pompiers.
	 * <p>
	 * Cette méthode permet de supprimer une caserne existante à partir de ses
	 * informations à l'endpoint /firestation.
	 * </p>
	 *
	 * @param deletedFirestation la caserne à supprimer
	 * @return ResponseEntity contenant un message de succès en cas de réussite ou
	 *         un message d'erreur en cas d'échec.
	 */
	@DeleteMapping("/firestation")
	public ResponseEntity<String> deleteFirestation(@RequestBody Firestation deletedFirestation) {
		logger.info("Suppression de la caserne : {}", deletedFirestation);
		firestationService.deleteFirestation(deletedFirestation);
		logger.info("La caserne a été supprimé avec succès.");
		logger.debug("La caserne : {} a été supprimée avec succès.", deletedFirestation);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Caserne supprimée avec succès.");
	}

	/**
	 * Met à jour une caserne de pompiers existante.
	 * <p>
	 * Cette méthode permet de mettre à jour les informations d'une caserne
	 * existante dans le système.
	 * </p>
	 *
	 * @param updatedFirestation la caserne avec les nouvelles informations
	 * @return ResponseEntity contenant un message de succès en cas de réussite ou
	 *         un message d'erreur en cas d'échec.
	 */
	@PutMapping("/firestation")
	public ResponseEntity<String> updateFirestation(@RequestBody Firestation updatedFirestation) {
		logger.info("Début de mise à jour de la caserne : {}", updatedFirestation);
		firestationService.updateFirestation(updatedFirestation);
		logger.info("Caserne mise à jour avec succès.");
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Caserne modifiée avec succès.");
	}

	/**
	 * Récupère les noms et numéros de téléphone des personnes couvertes par une
	 * caserne donnée.
	 * <p>
	 * Cette méthode prend un numéro de station en paramètre et retourne une liste
	 * de personnes associées à la caserne spécifiée à partir de l'endpoint
	 * /firestation.
	 * </p>
	 *
	 * @param stationNumber le numéro de la caserne.
	 * @return ResponseEntity contenant une liste de personnes associées à la
	 *         caserne en cas de succès ou un message d'erreur en cas d'echec.
	 */
	@GetMapping("/firestation")
	public ResponseEntity<?> personFromFirestation(@RequestParam String stationNumber) {
		logger.info("Recherche des personnes associées à la caserne : {}", stationNumber);
		List<String> persons = firestationService.personFromStationNumber(stationNumber);
		logger.info("La liste des personnes associées à la caserne a été récupérée avec succès");
		logger.debug("Liste des personnes associées à la caserne {} : {}", stationNumber, persons);
		return ResponseEntity.ok(persons);
	}

	/**
	 * Récupère les numéros de téléphone des personnes couverte par la caserne
	 * donnée.
	 * 
	 * <p>
	 * Cette Méthode prend en paramètre un numéro de station et retourne une liste
	 * de String contenant les numéro de téléphone des personnes associées à la
	 * caserne à partir de l'endpoint /phoneAlert.
	 * </p>
	 * 
	 * @param station le numéro de la caserne
	 * @return ResponseEntity contenant une liste de numéro de téléphone des
	 *         personnes associées à la caserne en cas de succès ou un message
	 *         d'erreur en cas d'echec.
	 */
	@GetMapping("/phoneAlert")
	public ResponseEntity<?> personToAlert(@RequestParam String station) {
		logger.info("Recherche des numéros de téléphone associés à la caserne : {}", station);
		List<String> phoneListAlert = firestationService.phoneAlert(station);
		logger.info("La liste des numéros de téléphone selon le numéro de station a été récupérée avec succès.");
		logger.debug("Liste des numéros de téléphone associés à la caserne {} : {}", station, phoneListAlert);
		return ResponseEntity.ok(phoneListAlert);
	}

}
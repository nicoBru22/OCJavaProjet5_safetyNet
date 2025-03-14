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
	 * Récupère la liste de toutes les casernes de pompiers.
	 *
	 * Cette méthode appelle le service pour obtenir toutes les casernes de pompiers et renvoie la liste dans une réponse HTTP avec un statut 200 OK.
	 *
	 * @return une réponse HTTP contenant la liste des casernes de pompiers
	 */
	@GetMapping("/firestations")
	public ResponseEntity<List<Firestation>> getAllFireStation() {
			logger.info("Récupération de toutes les casernes de pompiers.");
			List<Firestation> firestations = firestationService.getAllFireStations();
			return ResponseEntity.ok(firestations);
	}


	/**
	 * Ajoute une nouvelle caserne de pompiers.
	 *
	 * Cette méthode accepte une requête POST contenant une nouvelle caserne à ajouter. Elle appelle le service pour l'ajouter, puis renvoie une réponse HTTP indiquant que la caserne a été ajoutée avec succès.
	 *
	 * @param newFirestation l'objet Firestation à ajouter
	 * @return une réponse HTTP avec un statut 201 CREATED et un message de confirmation
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
	 *
	 * Cette méthode accepte une requête DELETE contenant une caserne à supprimer. Elle appelle le service pour supprimer la caserne, puis renvoie une réponse HTTP indiquant que la caserne a été supprimée avec succès.
	 *
	 * @param deletedFirestation l'objet Firestation à supprimer
	 * @return une réponse HTTP avec un statut 204 NO_CONTENT et un message de confirmation
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
	 * Met à jour les informations d'une caserne de pompiers.
	 *
	 * Cette méthode accepte une requête PUT contenant les informations mises à jour d'une caserne. Elle appelle le service pour effectuer la mise à jour, puis renvoie une réponse HTTP indiquant que la caserne a été modifiée avec succès.
	 *
	 * @param updatedFirestation l'objet Firestation avec les nouvelles informations
	 * @return une réponse HTTP avec un statut 204 NO_CONTENT et un message de confirmation
	 */
	@PutMapping("/firestation")
	public ResponseEntity<String> updateFirestation(@RequestBody Firestation updatedFirestation) {
		logger.info("Début de mise à jour de la caserne : {}", updatedFirestation);
		firestationService.updateFirestation(updatedFirestation);
		logger.info("Caserne mise à jour avec succès.");
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Caserne modifiée avec succès.");
	}

	/**
	 * Recherche les personnes associées à une caserne de pompiers en fonction du numéro de station.
	 *
	 * Cette méthode récupère la liste des personnes associées à une caserne donnée, en fonction du numéro de station, et renvoie une réponse HTTP contenant cette liste.
	 *
	 * @param stationNumber le numéro de la station pour rechercher les personnes associées
	 * @return une réponse HTTP contenant la liste des personnes associées à la caserne
	 */
	@GetMapping("/firestation")
	public ResponseEntity<List<String>> personFromFirestation(@RequestParam String stationNumber) {
		logger.info("Recherche des personnes associées à la caserne : {}", stationNumber);
		List<String> persons = firestationService.personFromStationNumber(stationNumber);
		logger.info("La liste des personnes associées à la caserne a été récupérée avec succès");
		logger.debug("Liste des personnes associées à la caserne {} : {}", stationNumber, persons);
		return ResponseEntity.ok(persons);
	}

	/**
	 * Récupère la liste des numéros de téléphone associés à une caserne de pompiers spécifiée.
	 * 
	 * Cette méthode interroge le service `firestationService` pour obtenir la liste des numéros de téléphone 
	 * des personnes associées à la caserne dont le numéro est fourni en paramètre.
	 * Si la liste est vide, une réponse HTTP avec un statut 204 (No Content) est renvoyée. 
	 * Si une erreur interne survient, une réponse avec un statut 500 (Internal Server Error) est retournée.
	 * 
	 * @param station Le numéro de la caserne pour laquelle les numéros de téléphone sont récupérés.
	 * @return ResponseEntity<?> La réponse contenant la liste des numéros de téléphone si elle est disponible, 
	 *         ou un statut d'erreur approprié en cas de problème.
	 *         - Si la liste des numéros est trouvée et non vide, un statut HTTP 200 (OK) avec la liste des numéros.
	 *         - Si la liste est vide, un statut HTTP 204 (No Content) est retourné.
	 *         - Si une erreur interne se produit, un statut HTTP 500 (Internal Server Error) est retourné.
	 */
	@GetMapping("/phoneAlert")
	public ResponseEntity<List<String>> personToAlert(@RequestParam String station) {
	    logger.info("Recherche des numéros de téléphone associés à la caserne : {}", station);
	    
	    List<String> phoneListAlert = firestationService.phoneAlert(station);
	    
	    logger.info("La liste des numéros de téléphone selon le numéro de station a été récupérée avec succès.");
	    logger.debug("Liste des numéros de téléphone associés à la caserne {} : {}", station, phoneListAlert);
	    return ResponseEntity.ok(phoneListAlert);
	}


}
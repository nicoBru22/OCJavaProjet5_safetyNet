package com.projet5.safetyNet.controller;

import java.util.List;
import java.util.Map;

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
	 * Cette méthode appelle le service pour obtenir toutes les casernes de pompiers
	 * et renvoie la liste dans une réponse HTTP avec un statut 200 OK.
	 *
	 * @return une réponse HTTP contenant la liste des casernes de pompiers
	 */
	@GetMapping("/firestations")
	public ResponseEntity<List<Firestation>> getAllFireStation() {
		logger.debug("Récupération de toutes les casernes de pompiers.");
		List<Firestation> firestations = firestationService.getAllFireStations();
		return ResponseEntity.ok(firestations);
	}

	/**
	 * Ajoute une nouvelle caserne de pompiers.
	 *
	 * Cette méthode accepte une requête POST contenant une nouvelle caserne à
	 * ajouter. Elle appelle le service pour l'ajouter, puis renvoie une réponse
	 * HTTP indiquant que la caserne a été ajoutée avec succès.
	 *
	 * @param newFirestation l'objet Firestation à ajouter
	 * @return une réponse HTTP avec un statut 201 CREATED et un message de
	 *         confirmation
	 */
	@PostMapping("/firestations")
	public ResponseEntity<String> addFirestation(@RequestBody Firestation newFirestation) {
		logger.debug("Ajout d'une nouvelle caserne : {}", newFirestation);
		firestationService.addFirestation(newFirestation);
		logger.info("La caserne a été ajoutée avec succès.");
		logger.debug("Caserne {} ajoutée avec succès.", newFirestation);
		return ResponseEntity.status(HttpStatus.CREATED).body("La caserne a été ajoutée avec succès !");
	}

	/**
	 * Supprime une caserne de pompiers.
	 *
	 * Cette méthode accepte une requête DELETE contenant une caserne à supprimer.
	 * Elle appelle le service pour supprimer la caserne, puis renvoie une réponse
	 * HTTP indiquant que la caserne a été supprimée avec succès.
	 *
	 * @param deletedFirestation l'objet Firestation à supprimer
	 * @return une réponse HTTP avec un statut 204 NO_CONTENT et un message de
	 *         confirmation
	 */
	@DeleteMapping("/firestations")
	public ResponseEntity<String> deleteFirestation(@RequestBody Firestation deletedFirestation) {
		logger.debug("Suppression de la caserne : {}", deletedFirestation);
		firestationService.deleteFirestation(deletedFirestation);
		logger.info("La caserne a été supprimé avec succès.");
		logger.debug("La caserne : {} a été supprimée avec succès.", deletedFirestation);
		return ResponseEntity.status(HttpStatus.OK).body("Caserne supprimée avec succès.");
	}

	/**
	 * Met à jour les informations d'une caserne de pompiers.
	 *
	 * Cette méthode accepte une requête PUT contenant les informations mises à jour
	 * d'une caserne. Elle appelle le service pour effectuer la mise à jour, puis
	 * renvoie une réponse HTTP indiquant que la caserne a été modifiée avec succès.
	 *
	 * @param updatedFirestation l'objet Firestation avec les nouvelles informations
	 * @return une réponse HTTP avec un statut 204 NO_CONTENT et un message de
	 *         confirmation
	 */
	@PutMapping("/firestations")
	public ResponseEntity<String> updateFirestation(@RequestBody Firestation updatedFirestation) {
		logger.debug("Début de mise à jour de la caserne : {}", updatedFirestation);
		firestationService.updateFirestation(updatedFirestation);
		logger.info("Caserne mise à jour avec succès.");
		return ResponseEntity.status(HttpStatus.OK).body("Caserne modifiée avec succès.");
	}

	/**
	 * Recherche les personnes associées à une caserne de pompiers en fonction du
	 * numéro de station.
	 *
	 * Cette méthode récupère la liste des personnes associées à une caserne donnée,
	 * en fonction du numéro de station, et renvoie une réponse HTTP contenant cette
	 * liste.
	 *
	 * @param stationNumber le numéro de la station pour rechercher les personnes
	 *                      associées
	 * @return une réponse HTTP contenant la liste des personnes associées à la
	 *         caserne
	 */
	@GetMapping("/firestation/person")
	public ResponseEntity<List<String>> personFromFirestation(@RequestParam String stationNumber) {
		logger.debug("Recherche des personnes associées à la caserne : {}", stationNumber);
		List<String> persons = firestationService.personFromStationNumber(stationNumber);
		logger.info("La liste des personnes associées à la caserne a été récupérée avec succès");
		logger.debug("Liste des personnes associées à la caserne {} : {}", stationNumber, persons);
		return ResponseEntity.ok(persons);
	}

	/**
	 * Récupère la liste des numéros de téléphone associés à une caserne donnée.
	 * 
	 * Cette méthode est appelée via une requête HTTP GET sur le chemin "/phoneAlert". Elle accepte un paramètre de requête 
	 * {@code station} qui spécifie la caserne pour laquelle les numéros de téléphone des personnes doivent être récupérés. 
	 * Elle utilise le service {@code firestationService} pour obtenir la liste des numéros de téléphone associés à cette caserne.
	 * 
	 * @param station Le numéro ou le nom de la caserne pour laquelle les numéros de téléphone doivent être récupérés.
	 * @return Une réponse HTTP contenant la liste des numéros de téléphone associés à la caserne spécifiée. La réponse 
	 *         est renvoyée avec un statut HTTP 200 (OK).
	 * @throws IllegalArgumentException Si la caserne spécifiée n'existe pas ou si la récupération des numéros de téléphone échoue.
	 */
	@GetMapping("/phoneAlert")
	public ResponseEntity<List<String>> personToAlert(@RequestParam String station) {
		logger.debug("Recherche des numéros de téléphone associés à la caserne : {}", station);

		List<String> phoneListAlert = firestationService.phoneAlert(station);

		logger.info("La liste des numéros de téléphone selon le numéro de station a été récupérée avec succès.");
		logger.debug("Liste des numéros de téléphone associés à la caserne {} : {}", station, phoneListAlert);
		return ResponseEntity.ok(phoneListAlert);
	}
	
	/**
	 * Recherche des personnes et des informations sur la caserne associée à une adresse donnée.
	 * 
	 * Cette méthode permet de récupérer la liste des personnes et les informations relatives à leur caserne 
	 * (médicaments, allergies, etc.) pour une adresse spécifiée dans la requête. 
	 * Les personnes sont regroupées par adresse, avec leur nom, numéro de téléphone, âge et antécédents médicaux.
	 * 
	 * @param address L'adresse de la caserne et des personnes à rechercher.
	 * @return Une réponse HTTP contenant une liste de maps, où chaque map représente une personne avec 
	 *         des détails comme leur nom, téléphone, âge, médicaments, allergies et la caserne associée.
	 *         La réponse est renvoyée avec un statut HTTP 200 (OK).
	 */
	@GetMapping("/fire")
	public ResponseEntity<List<Map<String, Object>>> personAndFirestationFromAddress(@RequestParam String address) {
	    logger.debug("Recherche de personnes et de la caserne selon l'adresse donnée.");
	    List<Map<String, Object>> info = firestationService.personAndFirestationFromAddress(address);
		logger.info("La liste a été récupérée avec succès.");
	    return ResponseEntity.ok(info);
	}

	
	/**
	 * Récupère la liste des casernes associées à un numéro de station donné.
	 * 
	 * Cette méthode permet de récupérer les casernes d'un certain numéro de station. Le paramètre `station`
	 * permet de filtrer les casernes et de récupérer uniquement celles qui correspondent à ce numéro.
	 * 
	 * @param station Le numéro de la station de caserne à rechercher.
	 * @return Une réponse HTTP contenant la liste des casernes correspondant à ce numéro de station. 
	 *         La réponse est renvoyée avec un statut HTTP 200 (OK).
	 */
	@GetMapping("/firestation")
	public ResponseEntity<List<Firestation>> firestationFromStationNumber(@RequestParam String station) {
	    logger.debug("Récupération de la liste des casernes à numéro de station donné.");
	    List<Firestation> firestations = firestationService.getFirestation(station);
	    logger.info("La liste des casernes à ce numéro a été récupérée avec succès.");
	    return ResponseEntity.ok(firestations);
	}	
	
	@GetMapping("/flood/station")
	public ResponseEntity<List<Map<String, Object>>> floodFromFirestation(@RequestParam String stationNumber) {
		logger.debug("Recherche des foyer selon l'adresse donnée.");
		List<Map<String, Object>> result = firestationService.floodFromFirestation(stationNumber);
		
		return ResponseEntity.ok(result);
		}

}
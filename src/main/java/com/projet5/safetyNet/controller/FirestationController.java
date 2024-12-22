package com.projet5.safetyNet.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.projet5.safetyNet.model.Firestation;
import com.projet5.safetyNet.service.FirestationService;

/**
 * Contrôleur REST pour la gestion des casernes de pompiers.
 * <p>
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
 * </p>
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
	 * <p>
	 * Cette méthode retourne une liste de toutes les casernes de pompiers
	 * existantes dans le système.
	 * </p>
	 *
	 * @return la liste des casernes de pompiers
	 */
	@GetMapping("/firestations")
	public List<Firestation> getAllFireStation() {
		logger.info("Récupération de toutes les casernes de pompiers.");
		List<Firestation> firestations = firestationService.getAllFireStations();
		logger.debug("Liste des casernes récupérée : {}", firestations);
		return firestations;
	}

	/**
	 * Ajoute une nouvelle caserne de pompiers.
	 * <p>
	 * Cette méthode permet d'ajouter une nouvelle caserne dans le système.
	 * </p>
	 *
	 * @param newFirestation la caserne à ajouter
	 */
	@PostMapping("/firestation")
	public void addFirestation(@RequestBody Firestation newFirestation) {
		logger.info("Ajout d'une nouvelle caserne : {}", newFirestation);
		firestationService.addFirestation(newFirestation);
		logger.debug("Caserne ajoutée avec succès.");
	}

	/**
	 * Supprime une caserne de pompiers.
	 * <p>
	 * Cette méthode permet de supprimer une caserne existante à partir de ses
	 * informations.
	 * </p>
	 *
	 * @param deletedFirestation la caserne à supprimer
	 * @throws Exception 
	 */
	@DeleteMapping("/firestation")
	public void deleteFirestation(@RequestBody Firestation deletedFirestation) throws Exception {
		logger.info("Suppression de la caserne : {}", deletedFirestation);
		firestationService.deleteFirestation(deletedFirestation);
		logger.debug("Caserne supprimée avec succès.");
	}

	/**
	 * Met à jour une caserne de pompiers existante.
	 * <p>
	 * Cette méthode permet de mettre à jour les informations d'une caserne
	 * existante dans le système.
	 * </p>
	 *
	 * @param updatedFirestation la caserne avec les nouvelles informations
	 */
	@PutMapping("/firestation")
	public void updateFirestation(@RequestBody Firestation updatedFirestation) {
		logger.info("Mise à jour de la caserne : {}", updatedFirestation);
		firestationService.updateFirestation(updatedFirestation);
		logger.debug("Caserne mise à jour avec succès.");
	}

	/**
	 * Récupère les noms et numéros de téléphone des personnes couvertes par une
	 * caserne donnée.
	 * <p>
	 * Cette méthode prend un numéro de station en paramètre et retourne une liste
	 * des noms et téléphones des personnes associées à la caserne spécifiée.
	 * </p>
	 *
	 * @param station le numéro de la caserne
	 * @return une liste des noms et téléphones des personnes associées à la caserne
	 * @throws Exception si une erreur se produit lors de la récupération des
	 *                   données
	 */
	@GetMapping("/firestation")
	public List<String> personFromFirestation(@RequestParam String stationNumber) throws Exception {
		logger.info("Recherche des personnes associées à la caserne : {}", stationNumber);
		List<String> persons = firestationService.personFromStationNumber(stationNumber);
		logger.debug("Liste des personnes associées à la caserne {} : {}", stationNumber, persons);
		return persons;
	}

	/**
	 * Récupère les numéros de téléphone des personnes couverte par la caserne
	 * donnée.
	 * 
	 * <p>
	 * Cette Méthode prend en paramètre un numéro de station et retourne une liste
	 * des numéro de téléphone associés à la caserne
	 * </p>
	 * 
	 * @param station le numéro de la caserne
	 * @return une liste de numéro de téléphone associé à la caserne
	 * @throws Exception si une erreur se produit lors de la récupération des
	 *                   données
	 */
	@GetMapping("/phoneAlert")
	public List<String> personToAlert(@RequestParam String station) throws Exception {
		logger.info("Recherche les numéros de téléphone associées à la caserne : {}", station);
		List<String> phoneListAlert = firestationService.phoneAlert(station);
		logger.debug("Liste des numéros de téléphone associées à la caserne {} : {}", station, phoneListAlert);
		return phoneListAlert;
	}
}
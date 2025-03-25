package com.projet5.safetyNet.service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.projet5.safetyNet.Exception.FirestationExistingException;
import com.projet5.safetyNet.Exception.FirestationNotFoundException;
import com.projet5.safetyNet.Exception.InvalidDateFormatException;
import com.projet5.safetyNet.Exception.InvalidRequestException;
import com.projet5.safetyNet.model.Firestation;
import com.projet5.safetyNet.model.Medicalrecord;
import com.projet5.safetyNet.model.Person;
import com.projet5.safetyNet.repository.FirestationRepository;
import com.projet5.safetyNet.repository.MedicalrecordRepository;
import com.projet5.safetyNet.repository.PersonRepository;

/**
 * Service pour gérer les opérations liées aux casernes de pompiers.
 * 
 * <p>
 * Cette classe permet d'effectuer des opérations CRUD (Create, Read, Update,
 * Delete) sur la liste des casernes de pompiers stockées dans le fichier de
 * données. Elle interagit avec les repositories pour effectuer les opérations
 * nécessaires de gestion des casernes et des personnes associées.
 * </p>
 * 
 * Voici les principales fonctionnalités de la classe :
 * <ul>
 * <li>Récupérer toutes les casernes.</li>
 * <li>Ajouter une nouvelle caserne.</li>
 * <li>Supprimer une caserne existante.</li>
 * <li>Mettre à jour les informations d'une caserne.</li>
 * <li>Récupérer les personnes associées à une station de pompiers donnée.</li>
 * <li>Récupère les numéros de téléphone des personnes associé à une
 * caserne.</li>
 * <li>Calcule l'âge d'une personne à partir de sa date de naissance.</li>
 * </ul>
 */
@Service
public class FirestationService {

	/**
	 * Le format de date utilisé dans l'application pour afficher ou parser les
	 * dates sous le format "dd/MM/yyyy". Il est utilisé pour convertir les dates en
	 * chaînes de caractères et inversement.
	 */
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	/**
	 * Le logger pour enregistrer les messages de log associés à la classe
	 * {@link FirestationService}. Il permet de suivre le comportement de
	 * l'application en produisant des logs d'informations, d'avertissements ou
	 * d'erreurs.
	 */
	private static final Logger logger = LogManager.getLogger(FirestationService.class);

	/**
	 * Le repository pour les casernes de pompiers. Il permet d'effectuer des
	 * opérations CRUD (Create, Read, Update, Delete) sur les entités représentant
	 * les casernes de pompiers.
	 */
	private final FirestationRepository firestationRepository;

	/**
	 * Le repository pour les personnes. Il permet d'effectuer des opérations CRUD
	 * sur les entités représentant les personnes.
	 */
	private final PersonRepository personRepository;

	/**
	 * Le repository pour les dossiers médicaux. Il permet d'effectuer des
	 * opérations CRUD sur les entités représentant les dossiers médicaux.
	 */
	private MedicalrecordRepository medicalrecordRepository;

	/**
	 * Constructeur du service pour initialiser les repositories nécessaires à la
	 * gestion des casernes de pompiers, des personnes et des dossiers médicaux.
	 *
	 * @param firestationRepository   Le repository pour les casernes de pompiers.
	 * @param personRepository        Le repository pour les personnes.
	 * @param medicalrecordRepository Le repository pour les dossiers médicaux.
	 */
	public FirestationService(FirestationRepository firestationRepository, PersonRepository personRepository,
			MedicalrecordRepository medicalrecordRepository) {
		this.firestationRepository = firestationRepository;
		this.personRepository = personRepository;
		this.medicalrecordRepository = medicalrecordRepository;
		logger.info("FirestationService, initialisé avec succès.");
	}

	/**
	 * Récupère la liste de toutes les casernes de pompiers.
	 *
	 * Cette méthode interroge le repository pour obtenir toutes les casernes
	 * enregistrées. Elle journalise les étapes du processus et génère un
	 * avertissement si aucune caserne n'est trouvée.
	 *
	 * @return une liste contenant toutes les casernes de pompiers, possiblement
	 *         vide si aucune caserne n'est enregistrée
	 */
	public List<Firestation> getAllFireStations() {
		logger.debug("Début de la récupération de toutes les firestations.");
		List<Firestation> firestations = firestationRepository.getAllFirestations();
		if (firestations.isEmpty()) {
			logger.warn("Aucune firestation trouvée.");
		}
		logger.info("{} firestations récupérées.", firestations.size());
		return firestations;
	}
	
	/**
	 * Récupère une liste de casernes à partir de l'identifiant de station.
	 * 
	 * @param station L'identifiant de la station à rechercher.
	 * @return Une liste de casernes correspondant à l'identifiant de station.
	 */
	public List<Firestation> getFirestation(String station) {
	    logger.debug("Début de la méthode getFirestation avec l'identifiant de station : {}", station);

	    List<Firestation> firestationList = getAllFireStations();

	    // Filtrer les casernes en fonction de l'identifiant de station
	    List<Firestation> filteredFirestations = firestationList.stream()
	            .filter(f -> f.getStation().equalsIgnoreCase(station))
	            .collect(Collectors.toList());

	    return filteredFirestations;
	}

	/**
	 * Ajoute une nouvelle caserne de pompiers.
	 *
	 * Cette méthode vérifie si les informations de la caserne sont valides (adresse
	 * et numéro de station). Elle vérifie également qu'aucune caserne n'existe déjà
	 * avec les mêmes informations. Si la caserne existe déjà, une exception est
	 * lancée. Si les informations sont valides et que la caserne n'existe pas déjà,
	 * la nouvelle caserne est ajoutée au repository.
	 *
	 * @param newFirestation la caserne à ajouter
	 * @throws InvalidRequestException      si l'adresse ou le numéro de station est
	 *                                      manquant ou invalide
	 * @throws FirestationExistingException si une caserne avec les mêmes
	 *                                      informations existe déjà
	 */
	public void addFirestation(Firestation newFirestation) {
		logger.debug("Début de l'ajout d'une nouvelle caserne : {}", newFirestation);
		if (newFirestation.getAddress() == null || newFirestation.getAddress().isEmpty()
				|| newFirestation.getStation() == null || newFirestation.getStation().isEmpty()) {
			logger.error("Données invalides : adresse ou numéro de station manquant.");
			throw new InvalidRequestException("Les champs adresse et numéro de station sont obligatoires.");
		}
		List<Firestation> firestationList = firestationRepository.getAllFirestations();
		logger.debug("la liste : " + firestationList);
		boolean firestationExist = firestationList.stream()
				.anyMatch(firestation -> firestation.getAddress().equalsIgnoreCase(newFirestation.getAddress())
						&& firestation.getStation().equalsIgnoreCase(newFirestation.getStation()));
		if (firestationExist) {
			logger.error("La caserne avec l'adresse '{}' et la station '{}' existe déjà.", newFirestation.getAddress(),
					newFirestation.getStation());
			throw new FirestationExistingException("La caserne existe déjà.");
		}
		firestationRepository.addFirestation(newFirestation);
		logger.info("Caserne ajoutée avec succès : {}", newFirestation);
	}

	/**
	 * Supprime une caserne de pompiers.
	 *
	 * Cette méthode vérifie si les informations de la caserne à supprimer sont
	 * valides (adresse et numéro de station). Si les informations sont invalides ou
	 * si la caserne n'existe pas, une exception est lancée. Si les informations
	 * sont valides et que la caserne existe, elle est supprimée du repository.
	 *
	 * @param deletedFirestation la caserne à supprimer
	 * @throws InvalidRequestException      si l'adresse ou le numéro de station est
	 *                                      manquant ou invalide
	 * @throws FirestationNotFoundException si la caserne n'existe pas
	 */
	public void deleteFirestation(Firestation deletedFirestation) {
		logger.debug("Début de la suppression de la caserne : {}", deletedFirestation);
		if (deletedFirestation == null || deletedFirestation.getAddress() == null
				|| deletedFirestation.getAddress().isEmpty() || deletedFirestation.getStation() == null
				|| deletedFirestation.getStation().isEmpty()) {
			logger.error("Données invalides : adresse ou numéro de station manquant.");
			throw new InvalidRequestException("Les champs adresse et station sont obligatoires.");
		}

		List<Firestation> firestationList = firestationRepository.getAllFirestations();
		boolean firestationExist = firestationList.stream()
				.anyMatch(firestation -> firestation.getAddress().equalsIgnoreCase(deletedFirestation.getAddress())
						&& firestation.getStation().equalsIgnoreCase(deletedFirestation.getStation()));
		if (!firestationExist) {
			logger.error("La caserne avec l'adresse {} et la station {} n'existe pas.", deletedFirestation.getAddress(),
					deletedFirestation.getStation());
			throw new FirestationNotFoundException("La caserne n'existe pas." + deletedFirestation);
		}
		firestationRepository.deleteFirestation(deletedFirestation);
		logger.info("Caserne supprimée avec succès : {}", deletedFirestation);
	}

	/**
	 * Met à jour les informations d'une caserne de pompiers.
	 *
	 * Cette méthode vérifie si l'adresse de la caserne à mettre à jour est valide.
	 * Si l'adresse est manquante ou invalide, une exception est lancée. Si la
	 * caserne n'existe pas à l'adresse fournie, une exception est également lancée.
	 * Si les vérifications passent, la caserne est mise à jour dans le repository.
	 *
	 * @param updatedFirestation la caserne avec les nouvelles informations
	 * @throws InvalidRequestException      si l'adresse est manquante ou invalide
	 * @throws FirestationNotFoundException si la caserne n'existe pas à l'adresse
	 *                                      spécifiée
	 */
	public void updateFirestation(Firestation updatedFirestation) {
		logger.debug("Début de la mise à jour d'une firestation : {}", updatedFirestation);
		if (updatedFirestation.getAddress() == null || updatedFirestation.getAddress().isEmpty()) {
			logger.error("Donnée invalide pour la mise à jour : adresse manquante ou nulle : {}", updatedFirestation);
			throw new InvalidRequestException("Le champ adresse est obligatoire." + updatedFirestation);
		}
		List<Firestation> firestationList = firestationRepository.getAllFirestations();
		boolean stationExists = firestationList.stream()
				.anyMatch(firestation -> firestation.getAddress().equalsIgnoreCase(updatedFirestation.getAddress()));
		if (!stationExists) {
			logger.error("La firestation n'existe pas à cette adresse : {}", updatedFirestation.getAddress());
			throw new FirestationNotFoundException("La firestation n'existe pas à cette adresse.");
		}
		firestationRepository.updateFirestation(updatedFirestation);
		logger.info("Firestation mise à jour avec succès : {}", updatedFirestation);
	}

	/**
	 * Récupère une liste d'informations sur les personnes associées à un numéro de
	 * station spécifique.
	 *
	 * Cette méthode filtre les personnes en fonction de leur adresse, qui est liée
	 * à une station donnée. Elle récupère toutes les personnes vivant à des
	 * adresses associées à la station spécifiée et fournit des informations
	 * détaillées sur ces personnes, y compris leur prénom, nom, numéro de
	 * téléphone, adresse, numéro de station et catégorie d'âge (enfant ou adulte).
	 * Elle retourne également un résumé du nombre total d'enfants et d'adultes pour
	 * la station.
	 *
	 * @param stationNumber le numéro de la station pour laquelle récupérer les
	 *                      personnes
	 * @return une liste contenant des informations détaillées sur les personnes
	 *         associées à la station, ainsi qu'un résumé du nombre d'adultes et
	 *         d'enfants
	 * @throws InvalidRequestException      si le numéro de station est vide ou nul
	 * @throws FirestationNotFoundException si aucune firestation n'est trouvée pour
	 *                                      le numéro de station spécifié
	 */
	public List<String> personFromStationNumber(String stationNumber) {
		logger.debug("Début de la récupération des personnes pour la station : {}", stationNumber);

		if (stationNumber == null || stationNumber.isEmpty()) {
			logger.error("Le numéro de station est vide ou nul.");
			throw new InvalidRequestException("Le numéro de station ne peut pas être vide.");
		}

		logger.debug("Initialisation des listes de données.");
		List<String> personFromFirestationList = new ArrayList<>();

		List<Firestation> firestationList = firestationRepository.getAllFirestations();
		List<Person> personList = personRepository.getAllPerson();
		List<Medicalrecord> medicalrecordList = medicalrecordRepository.getAllMedicalrecord();

		logger.debug("Filtrage des adresses pour la station : {}", stationNumber);
		List<String> filteredStationsAddress = firestationList.stream()
				.filter(firestation -> firestation.getStation().equals(stationNumber)).map(Firestation::getAddress)
				.collect(Collectors.toList());

		if (filteredStationsAddress.isEmpty()) {
			logger.error("Aucune firestation trouvée pour le numéro de station : {}", stationNumber);
			throw new FirestationNotFoundException("Il n'existe pas de firestation avec ce numéro.");
		}

		logger.debug("Filtrage des personnes résidant aux adresses trouvées : {}", filteredStationsAddress);
		List<Person> personFromFirestation = personList.stream()
				.filter(person -> filteredStationsAddress.contains(person.getAddress())).collect(Collectors.toList());

		if (personFromFirestation.isEmpty()) {
			logger.error("Aucune personne trouvée pour la station : {}", stationNumber);
		}

		AtomicInteger numChildren = new AtomicInteger(0);
		AtomicInteger numAdults = new AtomicInteger(0);

		for (Person person : personFromFirestation) {
			medicalrecordList.stream()
					.filter(record -> record.getFirstName().equals(person.getFirstName())
							&& record.getLastName().equalsIgnoreCase(person.getLastName()))
					.findFirst().ifPresent(record -> {
						String personInfo;
						int age = ageOfPerson(record.getBirthdate());
						String ageCategory = (age < 18) ? "Enfant" : "Adulte";

						if (age < 18) {
							numChildren.incrementAndGet();
						} else {
							numAdults.incrementAndGet();
						}

						personInfo = String.format("%s %s, %s, %s, Station: %s, %s, Age: %d ans", person.getFirstName(),
								person.getLastName(), person.getPhone(), person.getAddress(), stationNumber,
								ageCategory, age);

						personFromFirestationList.add(personInfo);
					});
		}

		String summary = String.format("Nombre total d'adultes : %d, Nombre total d'enfants : %d", numAdults.get(),
				numChildren.get());
		personFromFirestationList.add(summary);

		logger.info("Récupération terminée pour la station : {}. Résumé : {}", stationNumber, summary);
		return personFromFirestationList;
	}

	/**
	 * Récupère la liste des numéros de téléphone des personnes associées à une
	 * station donnée.
	 *
	 * Cette méthode utilise la méthode `personFromStationNumber` pour récupérer les
	 * informations des personnes résidant à une adresse associée à une station
	 * donnée. Elle extrait ensuite les numéros de téléphone des personnes et les
	 * retourne sous forme de liste. Si aucune personne n'est associée à la station
	 * ou si la liste des numéros de téléphone est vide, un avertissement est loggé.
	 *
	 * @param station le numéro de la station pour laquelle récupérer les numéros de
	 *                téléphone des personnes
	 * @return une liste contenant les numéros de téléphone des personnes associées
	 *         à la station
	 * @throws InvalidRequestException      si le numéro de station est vide ou nul
	 * @throws FirestationNotFoundException si aucune firestation n'est trouvée pour
	 *                                      le numéro de station spécifié
	 */
	public List<String> phoneAlert(String station) {
		logger.debug("Début de la récupération des personnes associées à la station : {}", station);
		List<String> personToAlert = personFromStationNumber(station);
		List<String> phoneListAlert = personToAlert.stream().map(personInfo -> personInfo.split(",")[1].trim())
				.collect(Collectors.toList());
		if (phoneListAlert.isEmpty()) {
			logger.error("La liste des numéro de téléphone pour la station {} est vide.", station);
		}
		logger.info("La liste des numéros de téléphone pour la station : {}  est : {}", station, phoneListAlert);
		return phoneListAlert;
	}

	/**
	 * Calcule l'âge d'une personne à partir de sa date de naissance.
	 *
	 * Cette méthode prend une date de naissance sous forme de chaîne de caractères
	 * et calcule l'âge en années en comparant cette date à la date actuelle. La
	 * date de naissance doit être dans le format spécifié par `DATE_FORMATTER`. Si
	 * le format est incorrect, une exception est levée.
	 *
	 * @param birthdate la date de naissance de la personne sous forme de chaîne de
	 *                  caractères
	 * @return l'âge de la personne en années
	 * @throws InvalidDateFormatException si le format de la date de naissance est
	 *                                    invalide
	 */
	public int ageOfPerson(String birthdate) {
		logger.debug("Tentative de calcul de l'âge pour la date de naissance : {}", birthdate);

		LocalDate birthDate;
		try {
			birthDate = LocalDate.parse(birthdate, DATE_FORMATTER);
		} catch (DateTimeParseException e) {
			logger.error("Erreur de format pour la date de naissance : {}. Format attendu : {}", birthdate,
					DATE_FORMATTER);
			throw new InvalidDateFormatException("Format de date invalide. Utilisez le format : " + DATE_FORMATTER);
		}

		int age = Period.between(birthDate, LocalDate.now()).getYears();
		logger.info("Âge calculé avec succès : {} ans pour la date de naissance {}", age, birthdate);

		return age;
	}
	
	/**
	 * Récupère la liste des personnes associées à une adresse, ainsi que les détails sur leur caserne, leurs médicaments et allergies.
	 * Le résultat est une liste de maps contenant ces informations pour chaque personne.
	 * 
	 * @param address L'adresse à rechercher dans la base de données pour lier les personnes et leur caserne.
	 * @return Une liste de maps où chaque map représente les détails d'une personne : prénom, nom, âge, téléphone, médicaments, allergies, et caserne.
	 * @throws InvalidRequestException Si l'adresse est nulle ou vide.
	 */
	public List<Map<String, Object>> personAndFirestationFromAddress(String address) {
	    logger.debug("Début de la méthode personAndFirestationFromAddress avec l'adresse : {}", address);

	    // Validation de l'adresse
	    if (address == null || address.isBlank()) {
	        logger.error("Adresse invalide : null ou vide");
	        throw new InvalidRequestException("Le champ address est obligatoire.");
	    }

	    List<Firestation> firestationsList = firestationRepository.getAllFirestations();
	    List<Person> personsList = personRepository.getAllPerson();
	    List<Medicalrecord> medicalrecordsList = medicalrecordRepository.getAllMedicalrecord();

	    Firestation firestation = firestationsList.stream()
	            .filter(f -> f.getAddress().equalsIgnoreCase(address))
	            .findFirst()
	            .orElse(null);

	    if (firestation != null) {
	        logger.info("Caserne trouvée pour l'adresse {} : Station {}", address, firestation.getStation());
	    } else {
	        logger.warn("Aucune caserne trouvée pour l'adresse {}", address);
	    }

	    List<Person> filteredPersons = personsList.stream()
	            .filter(person -> person.getAddress().equalsIgnoreCase(address))
	            .collect(Collectors.toList());

	    logger.info("Nombre de personnes trouvées à l'adresse {} : {}", address, filteredPersons.size());

	    List<Map<String, Object>> result = new ArrayList<>();

	    for (Person person : filteredPersons) {
	        Optional<Medicalrecord> medicalRecordOpt = medicalrecordsList.stream()
	                .filter(medicalrecord -> 
	                    medicalrecord.getFirstName().equalsIgnoreCase(person.getFirstName()) &&
	                    medicalrecord.getLastName().equalsIgnoreCase(person.getLastName()))
	                .findFirst();

	        List<String> medications = medicalRecordOpt.map(Medicalrecord::getMedications).orElse(Collections.emptyList());
	        List<String> allergies = medicalRecordOpt.map(Medicalrecord::getAllergies).orElse(Collections.emptyList());
	        
	        String birthdate = medicalRecordOpt.map(Medicalrecord::getBirthdate).orElse(null);
	        int age = ageOfPerson(birthdate);

	        Map<String, Object> personDetails = new LinkedHashMap<>();
	        personDetails.put("firstName", person.getFirstName());
	        personDetails.put("lastName", person.getLastName());
	        personDetails.put("age", age);
	        personDetails.put("phone", person.getPhone());
	        personDetails.put("medications", medications);
	        personDetails.put("allergies", allergies);
	        personDetails.put("address", person.getAddress());
	        personDetails.put("firestation", firestation != null ? firestation.getStation() : "Aucune caserne");

	        result.add(personDetails);

	        logger.debug("Ajouté : {} {} - Téléphone : {} - Caserne : {}",
	                person.getFirstName(), person.getLastName(), person.getPhone(), 
	                firestation != null ? firestation.getStation() : "Aucune");
	    }

	    logger.info("Fin de la méthode personAndFirestationFromAddress. Nombre total de résultats : {}", result.size());
	    
	    return result;
	}
	
	public List<Map<String, Object>> floodFromFirestation(String stationNumber) {
	    logger.debug("Début de la méthode floodFromFirestation avec le numéro de station : {}", stationNumber);

	    List<Firestation> firestationList = getFirestation(stationNumber);
	    
	    List<Map<String, Object>> result = new ArrayList<>();

	    // Récupérer les adresses des casernes
	    List<String> allFirestationAddresses = firestationList.stream()
	            .map(Firestation::getAddress)
	            .collect(Collectors.toList());
	    logger.debug("La liste des adresses des casernes : {}", allFirestationAddresses);
	    
	    // Créer un Map pour regrouper les personnes par adresse
	    Map<String, List<Map<String, Object>>> groupedByAddress = new HashMap<>();

	    // Parcourir chaque adresse de caserne et récupérer les personnes associées
	    for (String address : allFirestationAddresses) {
	        List<Map<String, Object>> personsAtAddress = personAndFirestationFromAddress(address);
	        
	        // Regrouper les personnes par adresse
	        groupedByAddress.put(address, personsAtAddress);
	        logger.debug("Ajouté les personnes de l'adresse : {}", address);
	    }

	    // Ajouter les informations regroupées dans le résultat final
	    for (Map.Entry<String, List<Map<String, Object>>> entry : groupedByAddress.entrySet()) {
	        Map<String, Object> addressInfo = new HashMap<>();
	        addressInfo.put("address", entry.getKey());
	        addressInfo.put("people", entry.getValue());

	        result.add(addressInfo);
	        logger.debug("Ajouté les personnes pour l'adresse {} : {}", entry.getKey(), entry.getValue());
	    }

	    logger.info("Fin de la méthode floodFromFirestation. Nombre total de résultats : {}", result.size());
	    
	    return result;
	}

	








}

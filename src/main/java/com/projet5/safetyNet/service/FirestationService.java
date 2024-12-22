package com.projet5.safetyNet.service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.projet5.safetyNet.model.Firestation;
import com.projet5.safetyNet.model.Medicalrecord;
import com.projet5.safetyNet.model.Person;
import com.projet5.safetyNet.repository.FirestationRepository;
import com.projet5.safetyNet.repository.MedicalrecordRepository;
import com.projet5.safetyNet.repository.PersonRepository;

/**
 * Service pour gérer les opérations liées aux casernes de pompiers.
 * <p>
 * Cette classe permet d'effectuer des opérations CRUD (Create, Read, Update,
 * Delete) sur la liste des casernes de pompiers stockées dans le fichier de
 * données. Elle interagit avec les repositories pour effectuer les opérations
 * nécessaires de gestion des casernes et des personnes associées.
 * </p>
 * 
 * Voici les principales fonctionnalités de la classe :
 * <ul>
 * <li>Ajouter une nouvelle caserne.</li>
 * <li>Supprimer une caserne existante.</li>
 * <li>Mettre à jour les informations d'une caserne.</li>
 * <li>Récupérer toutes les casernes.</li>
 * <li>Récupérer les personnes associées à une station de pompiers donnée.</li>
 * </ul>
 */
@Service
public class FirestationService {

	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private static final Logger logger = LogManager.getLogger(FirestationService.class);
	private final FirestationRepository firestationRepository;
	private final PersonRepository personRepository;
	private MedicalrecordRepository medicalrecordRepository;

	/**
	 * Constructeur du service pour initialiser les repositories nécessaires.
	 *
	 * @param firestationRepository   Le repository pour les casernes de pompiers.
	 * @param personRepository        Le repository pour les personnes.
	 * @param medicalrecordRepository
	 */
	public FirestationService(FirestationRepository firestationRepository, PersonRepository personRepository,
			MedicalrecordRepository medicalrecordRepository) {
		this.firestationRepository = firestationRepository;
		this.personRepository = personRepository;
		this.medicalrecordRepository = medicalrecordRepository;
	}

	/**
	 * Récupère la liste complète des casernes de pompiers.
	 * 
	 * @return Une liste d'objets {@link Firestation} représentant toutes les
	 *         casernes.
	 * @throws RuntimeException si une erreur se produit lors de la récupération des
	 *                          données.
	 */
	public List<Firestation> getAllFireStations() {
		try {
			logger.info("Début de la récupération de toutes les firestations.");
			List<Firestation> firestations = firestationRepository.getAllFirestations();
			if (firestations.isEmpty()) {
				logger.warn("Aucune firestation trouvée.");
			} else {
				logger.info("{} firestations récupérées.", firestations.size());
			}
			return firestations;
		} catch (Exception e) {
			logger.error("Erreur lors de la récupération des firestations : {}", e.getMessage(), e);
			throw new RuntimeException("Erreur lors de la récupération des firestations", e);
		}
	}

	/**
	 * Ajoute une nouvelle caserne de pompiers.
	 * 
	 * @param newFirestation L'objet {@link Firestation} à ajouter.
	 * @throws IllegalArgumentException Si les champs adresse ou station sont
	 *                                  invalides.
	 * @throws RuntimeException         Si une erreur se produit lors de l'ajout.
	 */
	public void addFirestation(Firestation newFirestation) {
		try {
			logger.info("Début de l'ajout d'une nouvelle firestation : {}", newFirestation);
			if (newFirestation.getAddress() == null || newFirestation.getAddress().isEmpty()
					|| newFirestation.getStation() == null || newFirestation.getStation().isEmpty()) {
				logger.error("Données invalides pour la firestation : adresse ou station manquante.");
				throw new IllegalArgumentException("Les champs adresse et station sont obligatoires.");
			}
			firestationRepository.addFirestation(newFirestation);
			logger.info("Firestation ajoutée avec succès : {}", newFirestation);
		} catch (IllegalArgumentException e) {
			logger.error("Données invalides pour la firestation : {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Erreur lors de l'ajout de la firestation : {}", e.getMessage(), e);
			throw new RuntimeException("Erreur lors de l'ajout de la firestation", e);
		}
	}

	/**
	 * Supprime une caserne de pompiers existante.
	 * 
	 * @param deletedFirestation L'objet {@link Firestation} à supprimer.
	 * @throws Exception 
	 * @throws IllegalArgumentException Si les champs adresse ou station sont
	 *                                  invalides.
	 * @throws RuntimeException         Si une erreur se produit lors de la
	 *                                  suppression.
	 */
	public void deleteFirestation(Firestation deletedFirestation) throws Exception {
		try {
			logger.info("Début de la suppression d'une firestation : {}", deletedFirestation);
			if (deletedFirestation == null || deletedFirestation.getAddress() == null
					|| deletedFirestation.getAddress().isEmpty() || deletedFirestation.getStation() == null
					|| deletedFirestation.getStation().isEmpty()) {
				logger.error("Données invalides pour la suppression de la firestation : adresse ou station manquante.");
				throw new IllegalArgumentException("Les champs adresse et station sont obligatoires.");
			}
			firestationRepository.deleteFirestation(deletedFirestation);
			logger.info("Firestation supprimée avec succès : {}", deletedFirestation);
		} catch (IllegalArgumentException e) {
			logger.error("Données invalides pour la suppression de la firestation : {}", e.getMessage());
			throw new Exception("Données invalides pour la suppression de la firestation : {}", e);
		} catch (Exception e) {
			logger.error("Erreur lors de la suppression de la firestation : {}", e.getMessage(), e);
			throw new RuntimeException("Erreur lors de la suppression de la firestation", e);
		}
	}

	/**
	 * Met à jour une caserne de pompiers existante.
	 * 
	 * @param updatedFirestation L'objet {@link Firestation} à mettre à jour.
	 * @throws IllegalArgumentException Si le champ adresse est invalide.
	 * @throws Exception                Si la caserne n'existe pas à cette adresse.
	 * @throws RuntimeException         Si une erreur se produit lors de la mise à
	 *                                  jour.
	 */
	public void updateFirestation(Firestation updatedFirestation) {
		try {
			logger.info("Début de la mise à jour d'une firestation : {}", updatedFirestation);
			if (updatedFirestation.getAddress() == null || updatedFirestation.getAddress().isEmpty()) {
				logger.error("Donnée invalide pour la mise à jour : adresse manquante.");
				throw new IllegalArgumentException("Le champ adresse est obligatoire.");
			}

			List<Firestation> firestationList = firestationRepository.getAllFirestations();
			boolean stationExists = firestationList.stream().anyMatch(
					firestation -> firestation.getAddress().equalsIgnoreCase(updatedFirestation.getAddress()));

			if (!stationExists) {
				logger.error("La firestation n'existe pas à cette adresse : {}", updatedFirestation.getAddress());
				throw new Exception("La firestation n'existe pas à cette adresse.");
			}

			firestationRepository.updateFirestation(updatedFirestation);
			logger.info("Firestation mise à jour avec succès : {}", updatedFirestation);
		} catch (IllegalArgumentException e) {
			logger.error("Données invalides pour la mise à jour de la firestation : {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Erreur lors de la mise à jour de la firestation : {}", e.getMessage(), e);
			throw new RuntimeException("Erreur lors de la mise à jour de la firestation", e);
		}
	}

	/**
	 * Récupère la liste des personnes associées à une station de pompiers donnée.
	 * <p>
	 * Cette méthode filtre les stations et les personnes associées à l'adresse de
	 * la station de pompiers puis retourne une liste d'informations sous forme de
	 * chaînes de caractères (prénom, nom, adresse).
	 * </p>
	 * 
	 * @param station Le numéro de la station de pompiers.
	 * @return Une liste de chaînes de caractères contenant les informations des
	 *         personnes liées à la station.
	 * @throws Exception Si une erreur se produit lors de la récupération des
	 *                   données ou du filtrage.
	 */
	public List<String> personFromStationNumber(String stationNumber) throws Exception {
		logger.info("Début de la récupération des personnes pour la station : {}", stationNumber);
		if (stationNumber == null || stationNumber.isEmpty()) {
			logger.error("Le numéro de station est vide ou nul.");
			throw new IllegalArgumentException("Le numéro de station ne peut pas être vide.");
		}

		try {
			List<String> personFromFirestationList = new ArrayList<>();

			List<Firestation> firestationList = firestationRepository.getAllFirestations();
			List<Person> personList = personRepository.getAllPerson();
			List<Medicalrecord> medicalrecordList = medicalrecordRepository.getAllMedicalrecord();

			// Récupération de l'adresse des stations à ce numéro stationNumber
			List<String> filteredStationsAddress = firestationList.stream()
					.filter(firestation -> firestation.getStation().equals(stationNumber)).map(Firestation::getAddress)
					.collect(Collectors.toList());
			if (filteredStationsAddress.isEmpty()) {
				logger.error("Aucune firestation trouvée pour le numéro de station : {}", stationNumber);
				throw new Exception("Il n'existe pas de firestation avec ce numéro.");
			}

			List<Person> personFromFirestation = personList.stream()
					.filter(person -> filteredStationsAddress.contains(person.getAddress()))
					.collect(Collectors.toList());

			if (personFromFirestation.isEmpty()) {
				logger.warn("Aucune personne trouvée pour la station : {}", stationNumber);
			}

			AtomicInteger numChildren = new AtomicInteger(0);
			AtomicInteger numAdults = new AtomicInteger(0);

			for (Person person : personFromFirestation) {
				medicalrecordList.stream()
						.filter(record -> record.getFirstName().equals(person.getFirstName())
								&& record.getLastName().equalsIgnoreCase(person.getLastName()))
						.findFirst().ifPresent(record -> {
							String personInfo;
							try {
								// Calculer l'âge de la personne
								int age = ageOfPerson(record.getBirthdate());

								// Déterminer la catégorie d'âge
								String ageCategory = (age < 18) ? "Enfant" : "Adulte";

								// Incrémenter les compteurs en fonction de l'âge
								if (age < 18) {
									numChildren.incrementAndGet(); // Incrémenter le compteur d'enfants
								} else {
									numAdults.incrementAndGet(); // Incrémenter le compteur d'adultes
								}

								// Construire la chaîne avec les informations personnelles
								personInfo = person.getFirstName() + " " + person.getLastName() + ", "
										+ person.getPhone() + ", " + person.getAddress() + ", Station: " + stationNumber
										+ ", " + ageCategory + ", Age: " + age + " ans";
							} catch (Exception e) {
								personInfo = person.getFirstName() + " " + person.getLastName() + ", "
										+ "Age information unavailable, Station: " + stationNumber;
								System.err.println("Erreur lors du calcul de l'âge pour " + person.getFirstName() + " "
										+ person.getLastName() + ": " + e.getMessage());
							}

							// Ajouter l'info de la personne sans le décompte
							personFromFirestationList.add(personInfo);
						});
			}

			String summary = "Nombre total d'adultes : " + numAdults.get() + ", Nombre total d'enfants : "
					+ numChildren.get();
			personFromFirestationList.add(summary); // Ajouter le résumé à la fin

			logger.info("Récupération terminée pour la station : {}", stationNumber);
			return personFromFirestationList;

		} catch (Exception e) {
			logger.error("Erreur lors de la récupération des personnes liées à la station : {}", stationNumber, e);
			throw new Exception("Erreur lors de la récupération des personnes liées à la station de pompiers.", e);
		}
	}

	/**
	 * * Récupère les numéros de téléphone des personnes couverte par la caserne
	 * donnée.
	 * 
	 * <p>
	 * Cette Méthode prend en paramètre un numéro de station et retourne une liste
	 * des numéro de téléphone associés à la caserne. Elle récupère la liste des
	 * personnes couverte par la caserne et extrait les numéros de téléphone.
	 * </p>
	 * 
	 * @param station le numéro de station de la caserne.
	 * @return une liste de numéro de téléphone associé à la caserne ou une liste
	 *         vide.
	 * @throws Exception si une erreur se produit lors de la récupération des
	 *                   données
	 */
	public List<String> phoneAlert(String station) throws Exception {
		logger.info("Début de la récupération des personnes associées à la station : {}", station);
		List<String> personToAlert = personFromStationNumber(station);
		if (personToAlert == null || personToAlert.isEmpty()) {
			logger.warn("Aucune personne trouvée pour la station : {}", station);
			return new ArrayList<>();
		}
		try {
			List<String> phoneListAlert = personToAlert.stream().map(personInfo -> personInfo.split(",")[1].trim())
					.collect(Collectors.toList());
			return phoneListAlert;
		} catch (Exception e) {
			logger.error("Erreur lors de la récupération des numéros de téléphone associés à la station : {}", station,
					e);
			throw new Exception(
					"Erreur lors de la récupération des numéros de téléphone associés à la station de pompiers.", e);
		}

	}

	public int ageOfPerson(String birthdate) throws Exception {
		try {
			LocalDate birthDate = LocalDate.parse(birthdate, DATE_FORMATTER);
			int age = Period.between(birthDate, LocalDate.now()).getYears();
			return age;
		} catch (DateTimeParseException e) {
			throw new Exception("Impossible de calculer l'âge de la perosnne.");
		}
	}

}

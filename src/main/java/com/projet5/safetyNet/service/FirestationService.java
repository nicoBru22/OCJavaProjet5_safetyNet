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

import com.projet5.safetyNet.Exception.FirestationExistingException;
import com.projet5.safetyNet.Exception.FirestationNotFoundException;
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
	}

	/**
	 * Récupère la liste complète des casernes de pompiers.
	 * 
	 * <p>
	 * Cette méthode permet de récupérer toutes les casernes de pompiers
	 * enregistrées dans le système. Elle effectue un appel au repository pour
	 * récupérer la liste des casernes. Si aucune caserne n'est trouvée, un
	 * avertissement est émis. En cas de succès, le nombre de casernes récupérées
	 * est loggé.
	 * </p>
	 * 
	 * @return Une liste d'objets {@link Firestation} représentant toutes les
	 *         casernes de pompiers disponibles. Si aucune caserne n'est trouvée,
	 *         une liste vide est retournée.
	 * @throws RuntimeException Si une erreur se produit lors de la récupération des
	 *                          données depuis le repository.
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
	 * <p>
	 * Cette méthode permet d'ajouter une caserne de pompiers en vérifiant d'abord
	 * que les données de l'objet {@link Firestation} fourni sont valides. Elle
	 * vérifie également que la caserne n'existe pas déjà dans le système avant
	 * d'effectuer l'ajout.
	 * </p>
	 * 
	 * @param newFirestation L'objet {@link Firestation} contenant les informations
	 *                       de la caserne à ajouter (adresse et numéro de station).
	 * @throws IllegalArgumentException Si les champs adresse ou numéro de station
	 *                                  sont invalides (null ou vides).
	 * @throws RuntimeException         Si une erreur inattendue se produit lors de
	 *                                  l'ajout.
	 * @throws Exception                Si la caserne existe déjà dans le système.
	 */
	public void addFirestation(Firestation newFirestation) {
		logger.info("Début de l'ajout d'une nouvelle caserne : {}", newFirestation);
		if (newFirestation.getAddress() == null || newFirestation.getAddress().isEmpty()
				|| newFirestation.getStation() == null || newFirestation.getStation().isEmpty()) {
			logger.error("Données invalides : adresse ou numéro de station manquant.");
			throw new InvalidRequestException("Les champs adresse et numéro de station sont obligatoires.");
		}
		List<Firestation> firestationList = firestationRepository.getAllFirestations();
		boolean firestationExist = firestationList.stream()
				.anyMatch(firestation -> firestation.getAddress().equalsIgnoreCase(newFirestation.getAddress())
						&& firestation.getStation().equalsIgnoreCase(newFirestation.getStation()));
		if (firestationExist) {
			logger.warn("La caserne avec l'adresse '{}' et la station '{}' existe déjà.",
					newFirestation.getAddress(), newFirestation.getStation());
			throw new FirestationExistingException("La caserne existe déjà.");
		}
		firestationRepository.addFirestation(newFirestation);
		logger.info("Caserne ajoutée avec succès : {}", newFirestation);
	}

	/**
	 * Supprime une caserne de pompiers existante.
	 * 
	 * <p>
	 * Cette méthode supprime une caserne de pompiers à partir des informations
	 * fournies dans l'objet {@link Firestation}. Elle vérifie que l'objet passé en
	 * paramètre contient des données valides (adresse et numéro de station non
	 * nulles et non vides) avant de rechercher la caserne dans le système. Si la
	 * caserne existe, elle est supprimée ; sinon, une exception est levée.
	 * </p>
	 * 
	 * @param deletedFirestation L'objet {@link Firestation} contenant les
	 *                           informations de la caserne à supprimer.
	 * @throws IllegalArgumentException Si les champs adresse ou numéro de station
	 *                                  sont invalides (nulles ou vides).
	 * @throws Exception                Si la caserne n'existe pas ou en cas
	 *                                  d'erreur lors de la suppression.
	 * @throws RuntimeException         Si une erreur inattendue se produit pendant
	 *                                  la suppression.
	 */
	public void deleteFirestation(Firestation deletedFirestation) {
		logger.info("Début de la suppression de la caserne : {}", deletedFirestation);
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
			logger.warn("La caserne avec l'adresse {} et la station {} n'existe pas.",
					deletedFirestation.getAddress(), deletedFirestation.getStation());
			throw new FirestationNotFoundException("La caserne n'existe pas."+ deletedFirestation);
		}
		firestationRepository.deleteFirestation(deletedFirestation);
		logger.info("Caserne supprimée avec succès : {}", deletedFirestation);
	}

	/**
	 * Met à jour une caserne de pompiers existante.
	 * 
	 * <p>
	 * Cette méthode met à jour une caserne existante à partir des données fournies
	 * en paramètre. Elle vérifie d'abord si les données passées sont nulles ou
	 * vides, puis s'assure que la caserne existe déjà dans le système. Enfin, elle
	 * utilise la méthode
	 * {@link com.projet5.safetyNet.repository.FirestationRepository#updateFirestation}
	 * pour effectuer la mise à jour.
	 * </p>
	 * 
	 * @param updatedFirestation L'objet {@link Firestation} à mettre à jour.
	 * @throws IllegalArgumentException Si le champ adresse est invalide.
	 * @throws Exception                Si la caserne n'existe pas à cette adresse.
	 * @throws RuntimeException         Si une erreur se produit lors de la mise à
	 *                                  jour.
	 */
	public void updateFirestation(Firestation updatedFirestation) {
		logger.info("Début de la mise à jour d'une firestation : {}", updatedFirestation);
		if (updatedFirestation.getAddress() == null || updatedFirestation.getAddress().isEmpty()) {
			logger.error("Donnée invalide pour la mise à jour : adresse manquante ou nulle : {}", updatedFirestation);
			throw new InvalidRequestException("Le champ adresse est obligatoire."+ updatedFirestation);
		}
		List<Firestation> firestationList = firestationRepository.getAllFirestations();
		boolean stationExists = firestationList.stream().anyMatch(
				firestation -> firestation.getAddress().equalsIgnoreCase(updatedFirestation.getAddress()));
		if (!stationExists) {
			logger.error("La firestation n'existe pas à cette adresse : {}", updatedFirestation.getAddress());
			throw new FirestationNotFoundException("La firestation n'existe pas à cette adresse.");
		}
		firestationRepository.updateFirestation(updatedFirestation);
		logger.info("Firestation mise à jour avec succès : {}", updatedFirestation);
	}

	/**
	 * Récupère la liste des personnes associées à une station de pompiers donnée.
	 * <p>
	 * Cette méthode filtre les stations et les personnes associées à l'adresse de
	 * la station de pompiers, puis retourne une liste d'informations détaillées
	 * sous forme de chaînes de caractères (prénom, nom, téléphone, adresse,
	 * catégorie d'âge et âge).
	 * </p>
	 * <p>
	 * Le résultat inclut également un résumé contenant le nombre total d'adultes et
	 * d'enfants.
	 * </p>
	 * 
	 * @param stationNumber Le numéro de la station de pompiers.
	 * @return Une liste de chaînes de caractères contenant les informations des
	 *         personnes liées à la station, ainsi qu'un résumé des catégories
	 *         d'âge.
	 * @throws IllegalArgumentException Si le numéro de station est vide ou nul.
	 * @throws Exception                Si une erreur se produit lors de la
	 *                                  récupération des données ou du filtrage.
	 */
	public List<String> personFromStationNumber(String stationNumber) throws Exception {
		logger.info("Début de la récupération des personnes pour la station : {}", stationNumber);

		if (stationNumber == null || stationNumber.isEmpty()) {
			logger.error("Le numéro de station est vide ou nul.");
			throw new IllegalArgumentException("Le numéro de station ne peut pas être vide.");
		}

		try {
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
				throw new Exception("Il n'existe pas de firestation avec ce numéro.");
			}

			logger.debug("Filtrage des personnes résidant aux adresses trouvées : {}", filteredStationsAddress);
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
								int age = ageOfPerson(record.getBirthdate());
								String ageCategory = (age < 18) ? "Enfant" : "Adulte";

								if (age < 18) {
									numChildren.incrementAndGet();
								} else {
									numAdults.incrementAndGet();
								}

								personInfo = String.format("%s %s, %s, %s, Station: %s, %s, Age: %d ans",
										person.getFirstName(), person.getLastName(), person.getPhone(),
										person.getAddress(), stationNumber, ageCategory, age);
							} catch (Exception e) {
								personInfo = String.format("%s %s, Age information unavailable, Station: %s",
										person.getFirstName(), person.getLastName(), stationNumber);
								logger.debug("Erreur lors du calcul de l'âge pour {} {} : {}", person.getFirstName(),
										person.getLastName(), e.getMessage());
							}

							personFromFirestationList.add(personInfo);
						});
			}

			String summary = String.format("Nombre total d'adultes : %d, Nombre total d'enfants : %d", numAdults.get(),
					numChildren.get());
			personFromFirestationList.add(summary);

			logger.info("Récupération terminée pour la station : {}. Résumé : {}", stationNumber, summary);
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
	 * @return phoneListAlert une liste de numéro de téléphone associé à la caserne
	 *         ou une liste vide.
	 * @throws Exception si une erreur se produit lors de la récupération des
	 *                   données
	 */
	public List<String> phoneAlert(String station) throws Exception {
		logger.info("Début de la récupération des personnes associées à la station : {}", station);

		try {
			List<String> personToAlert = personFromStationNumber(station);
			List<String> phoneListAlert = personToAlert.stream().map(personInfo -> personInfo.split(",")[1].trim())
					.collect(Collectors.toList());
			logger.debug("La liste des numéros de téléphone pour la station : {}  est : {}", station, phoneListAlert);
			return phoneListAlert;
		} catch (Exception e) {
			logger.error("Erreur lors de la récupération des numéros de téléphone associés à la station : {}", station,
					e);
			throw new Exception(
					"Erreur lors de la récupération des numéros de téléphone associés à la station de pompiers.", e);
		}

	}

	/**
	 * Calcule l'âge d'une personne à partir de sa date de naissance.
	 * 
	 * @param birthdate La date de naissance de la personne au format String. Le
	 *                  format attendu est défini par {@code DATE_FORMATTER}.
	 * @return age L'âge de la personne en années entières (en format int).
	 * @throws Exception Si la date de naissance fournie ne peut pas être parsée ou
	 *                   si le calcul échoue.
	 */
	public int ageOfPerson(String birthdate) throws Exception {
		logger.info("Tentative de calcul de l'âge pour la date de naissance : {}", birthdate);
		try {
			LocalDate birthDate = LocalDate.parse(birthdate, DATE_FORMATTER);
			int age = Period.between(birthDate, LocalDate.now()).getYears();
			logger.debug("Âge calculé avec succès : {} ans pour la date de naissance {}", age, birthdate);
			return age;
		} catch (DateTimeParseException e) {
			logger.error("Erreur de format pour la date de naissance : {}. Format attendu : {}", birthdate,
					DATE_FORMATTER);
			throw new Exception("Impossible de calculer l'âge de la personne.", e);
		}
	}

}

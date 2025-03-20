package com.projet5.safetyNet.service;

import com.projet5.safetyNet.Exception.InvalidRequestException;
import com.projet5.safetyNet.Exception.PersonExistingException;
import com.projet5.safetyNet.Exception.PersonNotFoundException;
import com.projet5.safetyNet.model.Medicalrecord;
import com.projet5.safetyNet.model.Person;
import com.projet5.safetyNet.repository.PersonRepository;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 * Service pour gérer les opérations liées aux personnes.
 * 
 * <p>
 * Cette classe permet d'effectuer des opérations CRUD (Create, Read, Update,
 * Delete) sur la liste des personnes stockées dans le fichier de données. Elle
 * interagit avec les repositories pour effectuer les opérations nécessaires de
 * gestion des personnes.
 * </p>
 * 
 * Voici les principales fonctionnalités de la classe :
 * <ul>
 * 	<li>Récupère toutes les personnes enregistrées dans la base de données.</li>
 * 	<li>Supprime une personne de la base de données en utilisant son prénom, nom et téléphone.</li>
 * 	<li>Ajoute une nouvelle personne dans la base de données.</li>
 * 	<li>Met à jour les informations d'une personne dans le système.</li>
 * 	<li>Récupère la liste des adresses email des personnes d'une ville donnée.</li>
 * 	<li>Récupère la liste des enfants vivant à une adresse donnée.</li>
 * 	<li> Récupère la liste des personnes vivant à une adresse donnée.</li>
 * 	<li>Normalise une adresse en la mettant en minuscule et en supprimant les espaces inutiles.</li>
 * 	<li>Vérifie si une personne est mineure en fonction de sa date de naissance.</li>
 * 	<li>Récupère les informations des personnes portant un nom de famille donné.</li>
 *  <li>Recherche des personnes par nom de famille.</li>
 * </ul>
 */
@Service
public class PersonService {

	/**
	 * Le formatteur de date utilisé pour convertir des dates en chaînes de
	 * caractères selon le format "dd/MM/yyyy". Ce format est utilisé pour manipuler
	 * les dates dans le service {@link PersonService}.
	 */
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	/**
	 * Le logger utilisé pour enregistrer les logs dans la classe
	 * {@link PersonService}. Ce logger permet de suivre et de diagnostiquer le
	 * fonctionnement du service en enregistrant des événements et des erreurs.
	 */
	private static Logger logger = LogManager.getLogger(PersonService.class);

	/**
	 * Le repository utilisé pour accéder aux données des personnes. Ce repository
	 * permet d'effectuer des opérations CRUD sur les informations relatives aux
	 * personnes.
	 */
	private final PersonRepository personRepository;

	/**
	 * Le service utilisé pour gérer les dossiers médicaux. Ce service est
	 * responsable de la gestion des dossiers médicaux associés aux personnes.
	 */
	private MedicalrecordService medicalrecordService;

	/**
	 * Constructeur du service {@link PersonService} pour initialiser les
	 * repositories et services nécessaires. Ce constructeur permet de configurer le
	 * service avec les repositories et services pour les personnes et les dossiers
	 * médicaux.
	 *
	 * @param personRepository     Le repository pour les personnes.
	 * @param medicalrecordService Le service pour gérer les dossiers médicaux.
	 */
	public PersonService(PersonRepository personRepository, MedicalrecordService medicalrecordService) {
		this.personRepository = personRepository;
		this.medicalrecordService = medicalrecordService;
		logger.info("PersonService, initialisé avec succès.");
	}

	/**
	 * Récupère toutes les personnes enregistrées dans la base de données.
	 * 
	 * Cette méthode interroge le repository pour obtenir la liste complète des personnes. Si aucune personne
	 * n'est trouvée dans la base de données, une exception {@link PersonNotFoundException} est levée.
	 * 
	 * @return Une liste contenant toutes les personnes présentes dans la base de données.
	 * 
	 * @throws PersonNotFoundException Si aucune personne n'est trouvée dans la base de données.
	 * 
	 * @see PersonNotFoundException
	 * @see PersonRepository#getAllPerson()
	 */
	public List<Person> getAllPersons() {
		logger.debug("Entrée dans la méthode getAllPersons de la classe PersonService");
		logger.debug("Début de la récupération de toutes les personnes.");
		List<Person> personList = personRepository.getAllPerson();
		if (personList.isEmpty()) {
			logger.error("Aucune personne trouvée.");
			throw new PersonNotFoundException("Aucune personne n'a été trouvée.");
		}
		logger.info("{} personnes récupérées.", personList.size());
		return personList;
	}

	/**
	 * Supprime une personne de la base de données en utilisant son prénom, nom et téléphone.
	 * 
	 * Cette méthode vérifie si une personne avec les informations fournies (prénom, nom et téléphone) existe
	 * dans la base de données. Si la personne n'existe pas, une exception {@link PersonNotFoundException} est levée.
	 * Si la personne existe, elle est supprimée en appelant la méthode {@link PersonRepository#deletePerson(String, String, String)}.
	 * 
	 * @param firstName Le prénom de la personne à supprimer.
	 * @param lastName Le nom de famille de la personne à supprimer.
	 * @param phone Le numéro de téléphone de la personne à supprimer.
	 * 
	 * @throws PersonNotFoundException Si la personne avec les informations spécifiées n'existe pas dans la base de données.
	 * 
	 * @see PersonNotFoundException
	 * @see PersonRepository#deletePerson(String, String, String)
	 */
	public void deletePerson(String firstName, String lastName, String phone) {
		logger.debug("Entrée dans la méthode deletePerson() de PersonService");
		logger.debug("Vérification si la personne existe bien en base de données.");
		logger.debug("La personne à vérifier est : " + firstName + " " + lastName + " " + phone);
		boolean personExists = personRepository.getAllPerson().stream()
				.anyMatch(person -> person.getFirstName().equalsIgnoreCase(firstName)
						&& person.getLastName().equalsIgnoreCase(lastName)
						&& person.getPhone().equalsIgnoreCase(phone));
		if (!personExists) {
			logger.error("La personne n'existe pas en base de données.");
			throw new PersonNotFoundException("La personne n'existe pas en base de données.");
		}
		logger.info(
				"La personne existe en base de données. Lancement de la méthode personRepository.deletePerson().");
		personRepository.deletePerson(firstName, lastName, phone);
	}

	/**
	 * Ajoute une nouvelle personne dans la base de données.
	 * 
	 * Cette méthode vérifie si les champs essentiels (prénom, nom, téléphone) de l'objet {@link Person}
	 * ne sont pas nuls ou vides. Ensuite, elle vérifie si une personne avec les mêmes informations
	 * (prénom, nom, téléphone) existe déjà dans la base de données. Si la personne existe déjà,
	 * une exception {@link PersonExistingException} est levée. Si la personne n'existe pas encore,
	 * elle est ajoutée dans la base de données.
	 * 
	 * @param newPerson L'objet {@link Person} à ajouter dans la base de données. Il ne doit pas être nul
	 *                  et doit contenir des valeurs valides pour les champs 'firstName', 'lastName' et 'phone'.
	 * 
	 * @throws InvalidRequestException Si l'objet {@link Person} est nul ou si l'un des champs 'firstName',
	 *                                  'lastName' ou 'phone' est vide ou nul.
	 * @throws PersonExistingException Si une personne avec les mêmes informations existe déjà dans la base de données.
	 * 
	 * @see Person
	 * @see InvalidRequestException
	 * @see PersonExistingException
	 */
	public void addPerson(Person newPerson) {
		logger.debug("Entrée dans la méthode addPerson de la classe PersonService.");
		logger.debug(
				"Vérification des champs 'firstName', 'lastName' et 'phone' pour s'assurer qu'ils ne sont pas nuls ou vides.");
		logger.debug("La personne à vérifier est : " + newPerson);
		
		if (newPerson.getFirstName() == null || newPerson.getFirstName().isEmpty()
				|| newPerson.getLastName() == null || newPerson.getLastName().isEmpty()
				|| newPerson.getPhone() == null || newPerson.getPhone().isEmpty()) {
			logger.error("Les champs 'firstName', 'lastName' et 'phone' sont obligatoires et ne peuvent être nuls ou vides : {}", newPerson);
			throw new InvalidRequestException("Les champs 'firstName', 'lastName' et 'phone' sont obligatoires et ne peuvent être nuls ou vides.");
		}
		
		boolean personExists = personRepository.getAllPerson().stream()
				.anyMatch(person -> person.getFirstName().equalsIgnoreCase(newPerson.getFirstName())
						&& person.getLastName().equalsIgnoreCase(newPerson.getLastName())
						&& person.getPhone().equals(newPerson.getPhone()));
		
		if (personExists) {
			logger.error("La personne {} {} avec le téléphone {} existe déjà dans la base de données.",
				    newPerson.getFirstName(), newPerson.getLastName(), newPerson.getPhone());
			throw new PersonExistingException("La personne existe déjà dans la base de données.");
		}
		logger.debug("La personne n'existe pas en base de donnée, donc elle peut être ajoutée.");
		personRepository.addPerson(newPerson);
		logger.info("Nouvelle personne ajoutée avec succès : {} ", newPerson);
	}

	/**
	 * Met à jour les informations d'une personne dans le système.
	 * 
	 * <p>
	 * Cette méthode permet de mettre à jour les informations d'une personne existante dans
	 * la base de données. Elle vérifie que les champs 'firstName' et 'lastName' sont fournis
	 * et non vides. Si ces informations sont absentes ou si la personne à mettre à jour n'existe
	 * pas dans la base de données, des exceptions spécifiques sont levées :
	 * - {@link InvalidRequestException} si les champs 'firstName' ou 'lastName' sont manquants.
	 * - {@link PersonNotFoundException} si la personne à mettre à jour n'est pas trouvée.
	 * </p>
	 * 
	 * @param updatedPerson L'objet contenant les informations mises à jour de la personne.
	 * @throws InvalidRequestException Si les champs 'firstName' ou 'lastName' sont null ou vides.
	 * @throws PersonNotFoundException Si la personne à mettre à jour n'existe pas dans la base de données.
	 */
	public void updatePerson(Person updatedPerson) {
		logger.debug("Entrée dans la méthode updatePerson() de la classe PersonService.");
		if (updatedPerson.getFirstName() == null || updatedPerson.getFirstName().isEmpty()
				|| updatedPerson.getLastName() == null || updatedPerson.getLastName().isEmpty()) {
			logger.error("Les champs 'firstName' ou 'lastName' sont null ou vides.");
			throw new InvalidRequestException(
					"Les champs 'firstName' et 'lastName' sont obligatoires pour une mise à jour.");
		}
		boolean personFound = personRepository.getAllPerson().stream()
				.anyMatch(person -> person.getFirstName().equalsIgnoreCase(updatedPerson.getFirstName())
						&& person.getLastName().equalsIgnoreCase(updatedPerson.getLastName()));
		if (!personFound) {
			logger.error("Cette personne n'existe pas : {}", updatedPerson);
			throw new PersonNotFoundException("Cette personne n'existe pas.");
		} 
		logger.debug("Personne trouvée. Mise à jour en cours...");
		personRepository.updatePerson(updatedPerson);
		logger.info("Personne mise à jour avec succès : {}", updatedPerson);
	}

	/**
	 * Récupère la liste des adresses email des personnes d'une ville donnée.
	 * 
	 * <p>
	 * Cette méthode prend le nom d'une ville en entrée et récupère les adresses email
	 * des personnes résidant dans cette ville. Si aucune personne n'est trouvée pour cette ville,
	 * une exception {@link PersonNotFoundException} est levée. Si le nom de la ville est invalide
	 * (nul ou vide), une exception {@link InvalidRequestException} est lancée.
	 * </p>
	 * 
	 * @param city Le nom de la ville pour laquelle les adresses email doivent être récupérées.
	 * @return Une liste de chaînes représentant les adresses email des personnes dans la ville spécifiée.
	 * @throws InvalidRequestException Si le paramètre 'city' est nul ou vide.
	 * @throws PersonNotFoundException Si aucune personne n'est trouvée dans la ville spécifiée.
	 */
	public List<String> getCommunityEmail(String city) {
		logger.debug("Début de la récupération des adresses email pour la ville : {}", city);

		if (city == null || city.isEmpty()) {
			logger.error("Le paramètre 'city' est nul ou vide.");
			throw new InvalidRequestException("Le champ 'city' ne peut pas être nul ou vide." + city);
		}

		List<Person> personList = personRepository.getAllPerson();
		logger.debug("Récupération des personnes terminée, traitement des données...");
		List<String> communityEmail = personList.stream().filter(person -> person.getCity().equalsIgnoreCase(city))
				.map(person -> person.getEmail()).collect(Collectors.toList());
		if (communityEmail.isEmpty()) {
			logger.warn("Aucune personne trouvée pour la ville : {}", city);
			throw new PersonNotFoundException("Aucune personne trouvée pour cette ville." + city);
		} 
		logger.info("Nombre d'adresses email récupérées pour la ville {} : {}", city, communityEmail.size());
		return communityEmail;
	}

	/**
	 * Récupère la liste des enfants vivant à une adresse donnée.
	 * 
	 * <p>
	 * Cette méthode prend une adresse en entrée, récupère les personnes vivant à cette adresse
	 * et cherche dans leurs dossiers médicaux pour déterminer si elles sont mineures (moins de 18 ans).
	 * Elle retourne une liste de chaînes formatées avec le nom, prénom et l'âge des enfants. 
	 * Si l'adresse est {@code null} ou vide, une exception est levée.
	 * </p>
	 * 
	 * @param address L'adresse des personnes à rechercher.
	 * @return Une liste de chaînes représentant les enfants (nom, prénom et âge). 
	 *         Si aucun enfant n'est trouvé, une liste vide est retournée.
	 * @throws InvalidRequestException Si l'adresse fournie est {@code null} ou vide.
	 */
	public List<String> getChildListFromAddress(String address) {
		logger.debug("Entrée dans la méthode getChildListFromAddress() de la class PersonService");
		
		if (address == null || address.isEmpty()) {
			logger.error("Le champ 'address' est obligatoire.");
			throw new InvalidRequestException("Le champ 'address' est obligatoire.");
		}
		
		logger.debug("Début de la récupération des enfants pour l'adresse : {}", address);
			String normalizedAddress = normalizeAddress(address);
			logger.debug("Adresse normalisée pour la comparaison : {}", normalizedAddress);

			List<Medicalrecord> medicalRecords = medicalrecordService.getAllMedicalrecord();
			logger.info("Nombre de dossiers médicaux récupérés : {}", medicalRecords.size());

			List<Person> personsAtAddress = listPersonByAddress(address);

			List<String> childrenAtAddress = personsAtAddress.stream().filter(person -> {
				Medicalrecord dossier = medicalRecords.stream()
						.filter(medical -> medical.getFirstName().equalsIgnoreCase(person.getFirstName())
								&& medical.getLastName().equalsIgnoreCase(person.getLastName()))
						.findFirst().orElse(null);

				if (dossier == null) {
					logger.debug("Aucun dossier médical trouvé pour {} {}", person.getFirstName(),
							person.getLastName());
					return false;
				}
	
				LocalDate birthDate = LocalDate.parse(dossier.getBirthdate(), DATE_FORMATTER);
				int age = Period.between(birthDate, LocalDate.now()).getYears();
				logger.debug("Âge calculé pour {} {} : {}", person.getFirstName(), person.getLastName(), age);
				return age < 18;
				
				}).map(person -> { Medicalrecord dossier = medicalRecords.stream()
							.filter(medical -> medical.getFirstName().equalsIgnoreCase(person.getFirstName())
									&& medical.getLastName().equalsIgnoreCase(person.getLastName()))
							.findFirst().orElse(null);
					if (dossier != null) {
						LocalDate birthDate = LocalDate.parse(dossier.getBirthdate(), DATE_FORMATTER);
						int age = Period.between(birthDate, LocalDate.now()).getYears();
						return person.getFirstName() + " " + person.getLastName() + ", " + age + " ans";
					}
					return person.getFirstName() + " " + person.getLastName();
				}).collect(Collectors.toList());
	
				if (childrenAtAddress.isEmpty()) {
					logger.error("Aucun enfant trouvé pour l'adresse : {}", address);
					return childrenAtAddress;
				}
			
			logger.info("Nombre d'enfants trouvés pour l'adresse {} : {}", address, childrenAtAddress.size());
			return childrenAtAddress;
	}

	
	/**
	 * Récupère la liste des personnes vivant à une adresse donnée.
	 * 
	 * <p>
	 * Cette méthode prend une adresse en entrée, normalise l'adresse fournie, puis 
	 * cherche dans le répertoire des personnes celles dont l'adresse correspond 
	 * à celle fournie. Si l'adresse est {@code null} ou vide, une exception est levée.
	 * Les adresses sont comparées après normalisation (mise en minuscules et suppression
	 * des espaces superflus) pour assurer des comparaisons cohérentes.
	 * </p>
	 * 
	 * @param address L'adresse de la personne à rechercher.
	 * @return Une liste de personnes vivant à l'adresse spécifiée.
	 * @throws InvalidRequestException Si l'adresse fournie est {@code null} ou vide.
	 */
	public List<Person> listPersonByAddress(String address) {		
		String normalizedAddress = normalizeAddress(address);
		logger.debug("Adresse normalisée pour la comparaison : {}", normalizedAddress);
		
		List<Person> persons = personRepository.getAllPerson();

		List<Person> personsAtAddress = persons.stream().filter(person -> {
			String normalizedPersonAddress = normalizeAddress(person.getAddress());
			logger.debug("Adresse de la personne {} {} : {}", person.getFirstName(), person.getLastName(),
					normalizedPersonAddress);
			return normalizedPersonAddress.equalsIgnoreCase(normalizedAddress);})
				.collect(Collectors.toList());
		
		logger.info("Liste des personnes trouvées à l'adresse {} : {}", normalizedAddress, personsAtAddress);
		
		return personsAtAddress;
	}

	/**
	 * Normalise une adresse en la mettant en minuscule et en supprimant les espaces inutiles.
	 * 
	 * <p>
	 * Cette méthode prend une adresse en entrée, supprime les espaces de début et de fin,
	 * puis convertit l'adresse en minuscules. Si l'adresse fournie est {@code null},
	 * une chaîne vide est retournée. Cette normalisation permet d'assurer une uniformité
	 * des données d'adresse, utile pour des comparaisons ou des traitements ultérieurs.
	 * </p>
	 * 
	 * @param address L'adresse à normaliser.
	 * @return L'adresse normalisée (en minuscules et sans espaces superflus), ou une chaîne vide si l'adresse est {@code null}.
	 */

	public String normalizeAddress(String address) {
		if (address == null) {
			logger.debug("Adresse fournie est nulle, retour d'une chaîne vide.");
			return "";
		}
		String normalizedAddress = address.trim().toLowerCase();
		logger.info("Adresse normalisée avec succès : {}", normalizedAddress);
		return normalizedAddress;
	}

	/**
	 * Vérifie si une personne est mineure en fonction de sa date de naissance.
	 * 
	 * <p>
	 * Cette méthode analyse la date de naissance fournie, calcule l'âge en années
	 * complètes et détermine si la personne est considérée comme un enfant 
	 * (moins de 18 ans).
	 * </p>
	 * 
	 * @param birthdate La date de naissance de la personne au format {@code yyyy-MM-dd}.
	 * @return {@code true} si la personne a moins de 18 ans, {@code false} sinon.
	fgvr */
	public Boolean isChild(String birthdate) {
		logger.debug("Début du parsing de la date de naissance.");
		LocalDate birthDate = LocalDate.parse(birthdate, DATE_FORMATTER);
		logger.info("Parsing effectué, calcul de l'âge.", birthDate);
		int age = Period.between(birthDate, LocalDate.now()).getYears();
		logger.info("Âge calculé pour la date de naissance {} obtenu avec succès : {}", birthdate, age);
		return age < 18;
	}

	/**
	 * Récupère les informations des personnes portant un nom de famille donné.
	 * 
	 * <p>
	 * Cette méthode recherche toutes les personnes correspondant au nom de famille 
	 * spécifié, puis enrichit leurs informations avec les données de leur dossier médical.
	 * Elle retourne une carte contenant :
	 * </p>
	 * <ul>
	 *   <li>Le nombre de personnes trouvées.</li>
	 *   <li>Une liste d'objets contenant les détails de chaque personne :
	 *       <ul>
	 *         <li>Prénom</li>
	 *         <li>Nom</li>
	 *         <li>Date de naissance</li>
	 *         <li>Adresse</li>
	 *         <li>Numéro de téléphone</li>
	 *         <li>Médicaments</li>
	 *         <li>Allergies</li>
	 *       </ul>
	 *   </li>
	 * </ul>
	 * 
	 * @param lastName Le nom de famille des personnes à rechercher.
	 * @return Une Map contenant le nombre de personne trouvée et les information sur les personnes.
	 */
	public Map<String, Object> personInfo(String lastName){
			logger.debug("Entrée dans la méthode personInfo() de personService avec lastName : {}", lastName);

			List<Map<String, Object>> personInfo = new ArrayList<>();
			List<Person> filteredPerson = listPersonByLastName(lastName);
			List<Medicalrecord> medicalrecordList = medicalrecordService.getAllMedicalrecord();

			for (Person person : filteredPerson) {
				medicalrecordList.stream()
						.filter(record -> record.getFirstName().equalsIgnoreCase(person.getFirstName())
								&& record.getLastName().equalsIgnoreCase(person.getLastName()))
						.forEach(record -> {
							Map<String, Object> info = new LinkedHashMap<>();
							info.put("firstName", person.getFirstName());
							info.put("lastName", person.getLastName());
							info.put("birthdate", record.getBirthdate());
							info.put("address", person.getAddress());
							info.put("phone", person.getPhone());
							info.put("medications", record.getMedications());
							info.put("allergies", record.getAllergies());

							personInfo.add(info);
						});
			}

			int count = personInfo.size();
			logger.info("Nombre de personnes trouvées pour le nom '{}': {}", lastName, count);

			Map<String, Object> result = new LinkedHashMap<>();
			result.put("count", count);
			result.put("personInfo", personInfo);

			logger.info("Récupération des informations avec succès : {}", result);
			return result;
	}
	
	/**
	 * Recherche des personnes par nom de famille.
	 *
	 * <p>Cette méthode permet de récupérer toutes les personnes dont le nom de famille 
	 * correspond à celui fourni en paramètre. Elle récupère d'abord la liste complète 
	 * des personnes depuis le repository, puis applique un filtre pour ne conserver que 
	 * celles ayant le nom spécifié, sans distinction entre majuscules et minuscules.
	 * </p>
	 *
	 * @param lastName Le nom de famille des personnes à rechercher.
	 * @return Une liste de personnes ayant le nom de famille spécifié, ou une liste vide si aucune correspondance n'est trouvée.
	 */
	public List<Person> listPersonByLastName(String lastName) {
	    logger.debug("Entrée dans la méthode listPersonByLastName() de personService avec lastName : {}", lastName);
	    List<Person> personList = personRepository.getAllPerson();
	    logger.info("Tentative de récupération d'une liste de personne selon le nom : {} ", lastName);
	    List<Person> filteredPerson = personList.stream()
	            .filter(person -> person.getLastName().equalsIgnoreCase(lastName))
	            .collect(Collectors.toList());
	    logger.info("Liste des personnes filtrées pour le nom de famille '{}': {}", lastName, filteredPerson);
	    return filteredPerson; // Correction ici, car tu retournais `personList` au lieu de `filteredPerson`
	}

}

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
 * <li>Récupérer une liste de toutes les personnes.</li>
 * <li>Ajouter une nouvelle personne.</li>
 * <li>Supprimer une personne existante.</li>
 * <li>Mettre à jour les informations d'une personne.</li>
 * <li>Récupérer une liste d'email des personnes selon leur ville.</li>
 * <li>Récupère une liste d'enfant selon l'adresse.</li>
 * <li>Savoir si une personne est un enfant ou non.</li>
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
	}

	/**
	 * Récupère la liste de toutes les personnes.
	 * 
	 * <p>
	 * Cette méthode permet de récupérer la liste de toutes les personnes présentes
	 * dans le système. Si la liste est vide, un avertissement est généré.
	 * </p>
	 * 
	 * @return {@code List<Person>} Liste des personnes.
	 * @throws Exception        si aucune personne n'a été trouvée.
	 * @throws RuntimeException si une erreur se produit lors de la récupération des
	 *                          données.
	 */
	public List<Person> getAllPersons() {
		logger.info("Entrée dans la méthode getAllPersons de la classe PersonService");
		logger.info("Début de la récupération de toutes les personnes.");
		List<Person> personList = personRepository.getAllPerson();
		if (personList.isEmpty()) {
			logger.warn("Aucune personne trouvée.");
			throw new PersonNotFoundException("Aucune personne n'a été trouvée.");
		}
			logger.debug("{} personnes récupérées.", personList.size());
		return personList;
	}

	/**
	 * Supprime une personne après avoir vérifié son existence dans la base de
	 * données.
	 * 
	 * <p>
	 * Cette méthode permet de supprimer une personne en vérifiant d'abord si cette
	 * personne existe dans la base de données en fonction de son prénom, nom et
	 * numéro de téléphone. Si la personne existe, la suppression est effectuée,
	 * sinon une exception est levée.
	 * </p>
	 * 
	 * @param firstName Le prénom de la personne à supprimer.
	 * @param lastName  Le nom de famille de la personne à supprimer.
	 * @param phone     Le numéro de téléphone de la personne à supprimer.
	 * @throws Exception        Si la personne n'existe pas dans la base de données.
	 * @throws RuntimeException Si une erreur inattendue se produit lors de la
	 *                          suppression de la personne.
	 */
	public void deletePerson(String firstName, String lastName, String phone) {
		logger.info("Entrée dans la méthode deletePerson() de PersonService");
			logger.info("Vérification si la personne existe bien en base de données.");
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
	 * Ajoute une nouvelle personne.
	 * 
	 * Cette méthode permet d'ajouter une nouvelle personne en effectuant les
	 * vérifications suivantes :
	 * <ul>
	 * <li>Les champs 'firstName', 'lastName' et 'phone' doivent être non nuls et
	 * non vides.</li>
	 * <li>Aucune personne avec les mêmes informations (prénom, nom, numéro de
	 * téléphone) ne doit déjà exister dans la base de données.</li>
	 * </ul>
	 * Si ces conditions sont respectées, la personne est ajoutée, sinon une
	 * exception est levée.
	 * 
	 * @param newPerson L'objet représentant la personne à ajouter. Il ne doit pas
	 *                  avoir de champs 'firstName', 'lastName' ou 'phone' vides.
	 * 
	 * @throws Exception        Si les champs obligatoires sont vides ou nuls, ou si
	 *                          une personne identique existe déjà dans la base de
	 *                          données.
	 * @throws RuntimeException Si une erreur imprévue survient lors de l'ajout de
	 *                          la personne.
	 */
	public void addPerson(Person newPerson) {
		logger.info("Entrée dans la méthode addPerson de la classe PersonService.");
		logger.info(
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
			logger.error("La personne existe déjà dans la base de données.");
			throw new PersonExistingException("La personne existe déjà dans la base de données.");
		}
		logger.info("La personne n'existe pas en base de donnée, donc elle peut être ajoutée.");
		personRepository.addPerson(newPerson);
		logger.debug("Nouvelle personne ajoutée avec succès : " + newPerson);
	}

	/**
	 * Met à jour les informations d'une personne existante dans la base de données.
	 * 
	 * <p>
	 * Cette méthode recherche une personne dans la base de données en utilisant son
	 * prénom et son nom. Si la personne existe, ses informations sont mises à jour
	 * avec les nouvelles données fournies. Si la personne n'existe pas ou si les
	 * champs obligatoires (prénom et nom) sont manquants, une exception est levée.
	 * </p>
	 * 
	 * @param updatedPerson L'objet représentant la personne avec les nouvelles
	 *                      informations. Le prénom et le nom doivent être
	 *                      renseignés pour procéder à la mise à jour.
	 * @throws Exception
	 * 
	 * @throws IllegalArgumentException Si les champs 'firstName' ou 'lastName' sont
	 *                                  vides ou nuls, ou si la personne n'existe
	 *                                  pas dans la base de données.
	 * @throws RuntimeException         Si une erreur imprévue survient lors de la
	 *                                  mise à jour de la personne.
	 */
	public void updatePerson(Person updatedPerson) {
		logger.info("Entrée dans la méthode updatePerson() de la classe PersonService.");
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
		logger.info("Personne trouvée. Mise à jour en cours...");
		personRepository.updatePerson(updatedPerson);
		logger.debug("Personne mise à jour avec succès : {}", updatedPerson);
	}

	/**
	 * Récupère la liste des adresses email des personnes résidant dans une ville
	 * spécifique.
	 * 
	 * <p>
	 * Cette méthode permet de récupérer toutes les adresses email des personnes qui
	 * résident dans la ville spécifiée en paramètre. Elle interroge la base de
	 * données pour obtenir la liste complète des personnes, puis filtre celles dont
	 * la ville correspond à celle fournie. Elle retourne une liste des adresses
	 * email des personnes qui habitent dans la ville donnée, ou une liste vide si
	 * aucune personne n'est trouvée.
	 * </p>
	 * 
	 * @param city Le nom de la ville dans laquelle rechercher les adresses email
	 *             des personnes.
	 * @return Une liste contenant les adresses email des personnes habitant dans la
	 *         ville spécifiée. Si aucune personne n'est trouvée pour cette ville,
	 *         une liste vide est retournée.
	 * @throws Exception Si une erreur survient lors de la récupération des données
	 *                   (par exemple, si la ville est invalide, ou une erreur
	 *                   technique survient lors de l'interrogation du repository).
	 */
	public List<String> getCommunityEmail(String city) {
		logger.info("Début de la récupération des adresses email pour la ville : {}", city);

		if (city == null || city.isEmpty()) {
			logger.error("Le paramètre 'city' est nul ou vide.");
			throw new InvalidRequestException("Le champ 'city' ne peut pas être nul ou vide." + city);
		}

		List<Person> personList = personRepository.getAllPerson();
		logger.info("Récupération des personnes terminée, traitement des données...");
		List<String> communityEmail = personList.stream().filter(person -> person.getCity().equalsIgnoreCase(city))
				.map(person -> person.getEmail()).collect(Collectors.toList());
		if (communityEmail.isEmpty()) {
			logger.warn("Aucune personne trouvée pour la ville : {}", city);
			throw new PersonNotFoundException("Aucune personne trouvée pour cette ville." + city);
		} 
		logger.debug("Nombre d'adresses email récupérées pour la ville {} : {}", city, communityEmail.size());
		return communityEmail;

		
	}

	/**
	 * Récupère la liste des enfants associés à une adresse donnée.
	 * 
	 * <p>
	 * Cette méthode prend en paramètre une adresse, vérifie sa validité, puis
	 * filtre les dossiers médicaux des personnes dont la date de naissance indique
	 * qu'elles sont mineures. Elle retourne une liste des prénoms, noms et âges des
	 * enfants associés à cette adresse.
	 * </p>
	 * 
	 * @param address l'adresse pour laquelle rechercher les enfants associés.
	 * @return une liste des informations des enfants (prénom, nom, âge) associés à
	 *         cette adresse. Si aucun enfant n'est trouvé pour cette adresse, une
	 *         liste vide est retournée.
	 * @throws Exception si une erreur survient lors de la récupération des données,
	 *                   par exemple si l'adresse est invalide, ou si une erreur
	 *                   technique survient lors de l'accès aux informations.
	 */
	public List<String> getChildListFromAddress(String address) {
		if (address == null || address.isEmpty()) {
			logger.error("Le champ 'address' est obligatoire.");
			throw new InvalidRequestException("Le champ 'address' est obligatoire.");
		}
		
		logger.info("Début de la récupération des enfants pour l'adresse : {}", address);
			String normalizedAddress = normalizeAddress(address);
			logger.debug("Adresse normalisée pour la comparaison : {}", normalizedAddress);

			List<Medicalrecord> medicalRecords = medicalrecordService.getAllMedicalrecord();
			logger.debug("Nombre de dossiers médicaux récupérés : {}", medicalRecords.size());

			List<Person> persons = personRepository.getAllPerson();
			logger.debug("Nombre de personnes récupérées : {}", persons.size());

			List<Person> personsAtAddress = persons.stream().filter(person -> {
				String normalizedPersonAddress = normalizeAddress(person.getAddress());
				logger.debug("Adresse de la personne {} {} : {}", person.getFirstName(), person.getLastName(),
						normalizedPersonAddress);
				return normalizedPersonAddress.equalsIgnoreCase(normalizedAddress);})
					.collect(Collectors.toList());
			logger.info("Nombre de personnes trouvées à l'adresse {} : {}", normalizedAddress, personsAtAddress.size());

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
			}).map(person -> {
				Medicalrecord dossier = medicalRecords.stream()
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
				logger.warn("Aucun enfant trouvé pour l'adresse : {}", address);
			} else {
				logger.info("Nombre d'enfants trouvés pour l'adresse {} : {}", address, childrenAtAddress.size());
			}
			return childrenAtAddress;
	}

	/**
	 * Normalise l'adresse en supprimant les espaces inutiles et en la convertissant
	 * en minuscules.
	 * 
	 * <p>
	 * Cette méthode prend une adresse en entrée, élimine les espaces avant et après
	 * l'adresse, puis la convertit en minuscules pour une comparaison cohérente.
	 * </p>
	 * 
	 * @param address l'adresse à normaliser.
	 * @return l'adresse normalisée, ou une chaîne vide si l'adresse est null.
	 */
	public String normalizeAddress(String address) {
		if (address == null) {
			logger.debug("Adresse fournie est null, retour d'une chaîne vide.");
			return "";
		}
		String normalizedAddress = address.trim().toLowerCase();
		logger.debug("Adresse normalisée : {}", normalizedAddress);
		return normalizedAddress;
	}

	/**
	 * Vérifie si une personne est un enfant en fonction de sa date de naissance.
	 * 
	 * <p>
	 * Cette méthode prend une date de naissance sous forme de chaîne de caractères,
	 * la convertit en un objet {@link LocalDate}, puis calcule l'âge de la
	 * personne. Si l'âge est inférieur à 18 ans, la méthode retourne {@code true},
	 * sinon elle retourne {@code false}.
	 * </p>
	 * 
	 * @param birthdate la date de naissance au format "yyyy-MM-dd".
	 * @return {@code true} si la personne est un enfant (moins de 18 ans),
	 *         {@code false} si la personne n'est pas un enfant ou si une erreur
	 *         survient lors du parsing de la date.
	 */
	public Boolean isChild(String birthdate) {
		logger.info("Début du parsing de la date de naissance.");
		LocalDate birthDate = LocalDate.parse(birthdate, DATE_FORMATTER);
		logger.info("Parsing effectué, calcul de l'âge.", birthDate);
		int age = Period.between(birthDate, LocalDate.now()).getYears();
		logger.debug("Âge calculé pour la date de naissance {} : {}", birthdate, age);
		return age < 18;
	}

	/**
	 * Récupère les informations d'une personne en fonction de son nom de famille.
	 * 
	 * <p>
	 * Cette méthode prend un nom de famille en entrée, filtre les personnes
	 * correspondant à ce nom, puis associe les informations de chaque personne à
	 * son dossier médical. Elle retourne une carte contenant le nombre de personnes
	 * trouvées et une liste des informations des personnes (nom, prénom, date de
	 * naissance, adresse, téléphone, médicaments et allergies).
	 * </p>
	 * 
	 * @param lastName le nom de famille de la personne à rechercher.
	 * @return une carte contenant le nombre de personnes trouvées et une liste
	 *         d'informations détaillées sur chaque personne.
	 * @throws Exception si une erreur survient lors de la récupération des données.
	 */
	public Map<String, Object> personInfo(String lastName){
			logger.info("Entrée dans la méthode personInfo() de personService avec lastName : {}", lastName);

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

			logger.debug("Le contenu de result : {}", result);
			return result;
	}
	
	public List<Person> listPersonByLastName(String lastName) {
		logger.info("Entrée dans la méthode listPersonByLastName() de personService avec lastName : {}", lastName);
		List<Person> personList = personRepository.getAllPerson();
		logger.info("Tentative de récupération d'une liste de personne selon le nom : {} ", lastName);
		List<Person> filteredPerson = personList.stream()
				.filter(person -> person.getLastName().equalsIgnoreCase(lastName))
				.collect(Collectors.toList());
		logger.debug("Liste des personnes filtrées pour le nom de famille '{}': {}", lastName, filteredPerson);
		return personList;
	}

}

package com.projet5.safetyNet.service;

import com.projet5.safetyNet.model.Medicalrecord;
import com.projet5.safetyNet.model.Person;
import com.projet5.safetyNet.repository.PersonRepository;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private static Logger logger = LogManager.getLogger(PersonService.class);
	private final PersonRepository personRepository;
	private MedicalrecordService medicalrecordService;

	public PersonService(PersonRepository personRepository, MedicalrecordService medicalrecordService) {
		this.personRepository = personRepository;
		this.medicalrecordService = medicalrecordService;
	}

	/**
	 * Méthode pour récupérer la liste de toutes les personnes.
	 * 
	 * @return {@code List<Person>} Liste des personnes.
	 * @throws RuntimeException si une erreur se produit lors de la récupération des
	 *                          données.
	 */
	public List<Person> getAllPersons() {
		logger.info("Entrée dans la méthode getAllPersons de la class PersonService");
		try {
			logger.info("Appel à personRepository.getAllPersons() pour récupérer toutes les personnes.");
			return personRepository.getAllPerson();
		} catch (Exception e) {
			logger.error("Erreur lors de l'exécution de personRepository.getAllPersons() : ", e.getMessage(), e);
			throw new RuntimeException("Erreur lors de la récupération des personnes.", e);
		}
	}

	/**
	 * Méthode pour supprimer une personne. Vérifie d'abord si la personne existe
	 * avant de tenter la suppression.
	 * 
	 * @param firstName Chaine de caractère correspondant au prénom de la personne.
	 * @param lastName  Chaine de caractère correspondant au nom de la personne.
	 * @param phone     Chaine de caractère correspondant au numéro de téléphone de
	 *                  la personne.
	 * @throws Exception
	 */
	public void deletePerson(String firstName, String lastName, String phone) throws Exception {
		logger.info("Entrée dans la méthode deletePerson() de PerosnService");
		try {
			logger.info("Vérification si la personne existe bien en base de donnée");
			logger.info("La personne à vérifier est : " + firstName + " " + lastName + " " + phone);
			boolean personExists = personRepository.getAllPerson().stream()
					.anyMatch(person -> person.getFirstName().equalsIgnoreCase(firstName)
							&& person.getLastName().equalsIgnoreCase(lastName)
							&& person.getPhone().equalsIgnoreCase(phone));
			if (personExists) {
				logger.info(
						"La personne existe en base de donnée. Lancement de la méthode personRepository.deletePerson() ");
				personRepository.deletePerson(firstName, lastName, phone);
			} else {
				logger.error("La personne n'existe pas en base de donnée.");
				throw new Exception("La personne n'existe pas en base de donnée.");
			}
		} catch (Exception e) {
			logger.error("Erreur lors de la suppression de la personne. ", e);
			throw new Exception("Erreur lors de la suppression de la personne : ", e);
		}
	}

	/**
	 * Méthode pour ajouter une nouvelle personne. Vérifie que les champs
	 * obligatoires sont présents et qu'une personne identique n'existe pas déjà.
	 * 
	 * @param newPerson est un objet de type Personne correspondant à la nouvelle
	 *                  personne à ajouter.
	 * @throws Exception
	 */
	public void addPerson(Person newPerson) throws Exception {
		logger.info("Entrée dans la méthode addPerson de la class PersonService.");

		try {
			logger.info("Vérification si les champs firestName et LastName et phone ne sont pas null ou vide.");
			logger.info("La perosnne à vérifier est : " + newPerson);
			if (newPerson.getFirstName() == null || newPerson.getFirstName().isEmpty()
					|| newPerson.getLastName() == null || newPerson.getLastName().isEmpty()
					|| newPerson.getPhone() == null || newPerson.getPhone().isEmpty()) {
				logger.error(
						"Les champs 'firstName', 'lastName' et 'phone' sont null ou vide alors qu'ils sont obligatoires.");
				throw new Exception(
						"Les champs 'firstName', 'lastName' et 'phone' sont null ou vide alors qu'ils sont obligatoires.");
			}
			try {
				boolean personExists = personRepository.getAllPerson().stream()
						.anyMatch(person -> person.getFirstName().equalsIgnoreCase(newPerson.getFirstName())
								&& person.getLastName().equalsIgnoreCase(newPerson.getLastName())
								&& person.getPhone().equals(newPerson.getPhone()));

				if (!personExists) {
					logger.info("Appel de la méthode personRepository.addPerson.");
					personRepository.addPerson(newPerson);
					logger.info("Nouvelle personne ajoutée avec succès : ", newPerson);
				} else {
					logger.error("La personne ne peut être ajoutée car elle existe déjà en base de donnée.");
					throw new Exception("La personne ne peut être ajoutée car elle existe déjà en base de donnée.");
				}
			} catch (Exception e) {
				logger.error("Erreur lors de l'ajout de la personne.", e);
				throw new Exception("Erreur lors de l'ajout de la personne.", e);
			}

		} catch (Exception e) {
			logger.error("Erreur lors de l'ajout de la personne.", e);
			throw new Exception("Erreur lors de l'ajout de la personne.", e);
		}
	}

	/**
	 * Méthode pour mettre à jour les informations d'une personne existante.
	 * Recherche une correspondance par prénom et nom, puis met à jour les
	 * informations.
	 * 
	 * @param updatedPerson Personne avec les nouvelles informations.
	 * @throws IllegalArgumentException Si les champs obligatoires sont manquants ou
	 *                                  si la personne n'existe pas.
	 */
	public void updatePerson(Person updatedPerson) {
		logger.info("Entrée dans la méthode updatePerson() de la classe PersonService.");

		if (updatedPerson.getFirstName() == null || updatedPerson.getFirstName().isEmpty()
				|| updatedPerson.getLastName() == null || updatedPerson.getLastName().isEmpty()) {
			logger.error("Les champs firstName ou lastName sont null ou vides.");
			throw new IllegalArgumentException(
					"Les champs 'firstName' et 'lastName' sont obligatoires pour une mise à jour.");
		}

		try {
			boolean personFound = personRepository.getAllPerson().stream()
					.anyMatch(person -> person.getFirstName().equalsIgnoreCase(updatedPerson.getFirstName())
							&& person.getLastName().equalsIgnoreCase(updatedPerson.getLastName()));

			if (personFound) {
				logger.info("Personne trouvée. Mise à jour en cours...");
				personRepository.updatePerson(updatedPerson);
				logger.info("Personne mise à jour avec succès : {}", updatedPerson);
			} else {
				logger.error("Cette personne n'existe pas : {}", updatedPerson);
				throw new IllegalArgumentException("Cette personne n'existe pas.");
			}
		} catch (Exception e) {
			logger.error("Erreur lors de la mise à jour de la personne.", e);
			throw new RuntimeException("Erreur lors de la mise à jour de la personne : " + e.getMessage(), e);
		}
	}

	/**
	 * Récupère la liste des adresses email des personnes selon leur ville.
	 * 
	 * <p>
	 * Cette méthode permet de récupérer la liste des adresses email des personnes
	 * qui résident dans la ville spécifiée en paramètre. Elle interroge la base de
	 * données pour récupérer toutes les personnes, puis filtre celles qui vivent
	 * dans la ville donnée. Elle retourne une liste contenant les adresses email
	 * des personnes habitant dans la ville spécifiée.
	 * </p>
	 * 
	 * @param city le nom de la ville dans laquelle rechercher les adresses email
	 *             des personnes.
	 * @return une liste contenant les adresses email des personnes vivant dans la
	 *         ville spécifiée, ou une liste vide si aucune personne n'est trouvée
	 *         pour cette ville.
	 * @throws Exception si une erreur survient lors de la récupération des données
	 *                   (par exemple, si la ville est invalide, ou une erreur
	 *                   technique survient dans la récupération des données depuis
	 *                   le repository).
	 */
	public List<String> getCommunityEmail(String city) throws Exception {
		logger.info("Début de la récupération des adresses email pour la ville : {}", city);

		if (city == null || city.isEmpty()) {
			logger.error("Le paramètre 'city' est vide ou nul.");
			throw new Exception("Le champ 'city' ne peut pas être null ou vide.");
		}

		try {
			List<Person> personList = personRepository.getAllPerson();
			logger.info("Récupération des personnes terminée, traitement des données...");
			List<String> communityEmail = personList.stream().filter(person -> person.getCity().equalsIgnoreCase(city))
					.map(person -> person.getEmail()).collect(Collectors.toList());
			if (communityEmail.isEmpty()) {
				logger.warn("Aucune personne trouvée pour la ville : {}", city);
			} else {
				logger.info("Nombre d'adresses email récupérées pour la ville {} : {}", city, communityEmail.size());
			}

			return communityEmail;

		} catch (Exception e) {
			logger.error("Une erreur s'est produite lors de la récupération des adresses email pour la ville : {}",
					city, e);
			throw new Exception("Une erreur s'est produite dans la récupération des données.", e);
		}
	}

	/**
	 * Récupère la liste des enfants d'une adresse donnée.
	 * 
	 * <p>
	 * Cette méthode prend une adresse en paramètre, vérifie sa validité, puis
	 * filtre les dossiers médicaux des personnes dont la date de naissance
	 * correspond à un enfant. Elle retourne une liste des prénoms, noms et dates de
	 * naissance des enfants associés à cette adresse.
	 * </p>
	 * 
	 * @param address l'adresse de la personne pour laquelle rechercher les enfants.
	 * @return une liste des informations des enfants (prénom, nom, date de
	 *         naissance) associés à cette adresse.
	 * @throws Exception si une erreur survient lors de la récupération des données
	 *                   (par exemple, si l'adresse est invalide ou si une erreur
	 *                   technique survient dans la récupération des données).
	 */
	public List<String> getChildListFromAddress(String address) throws Exception {
		if (address == null || address.isEmpty()) {
			logger.error("Le champ 'address' est obligatoire.");
			throw new Exception("Le champ 'address' est obligatoire.");
		}

		logger.info("Début de la récupération des enfants pour l'adresse : {}", address);

		try {
			// Normalisation de l'adresse
			String normalizedAddress = normalizeAddress(address);
			logger.debug("Adresse normalisée pour la comparaison : {}", normalizedAddress);

			// Récupération des données
			List<Medicalrecord> medicalRecords = medicalrecordService.getAllMedicalrecord();
			logger.debug("Nombre de dossiers médicaux récupérés : {}", medicalRecords.size());

			List<Person> persons = personRepository.getAllPerson();
			logger.debug("Nombre de personnes récupérées : {}", persons.size());

			// Filtrer les personnes à l'adresse spécifiée
			List<Person> personsAtAddress = persons.stream().filter(person -> {
				String normalizedPersonAddress = normalizeAddress(person.getAddress());
				logger.debug("Adresse de la personne {} {} : {}", person.getFirstName(), person.getLastName(),
						normalizedPersonAddress);
				return normalizedPersonAddress.equalsIgnoreCase(normalizedAddress);
			}).collect(Collectors.toList());
			logger.info("Nombre de personnes trouvées à l'adresse {} : {}", normalizedAddress, personsAtAddress.size());

			// Filtrer les enfants et inclure leur âge
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

				try {
					LocalDate birthDate = LocalDate.parse(dossier.getBirthdate(), DATE_FORMATTER);
					int age = Period.between(birthDate, LocalDate.now()).getYears();
					logger.debug("Âge calculé pour {} {} : {}", person.getFirstName(), person.getLastName(), age);
					return age < 18;
				} catch (DateTimeParseException e) {
					logger.error("Erreur de parsing pour la date de naissance : {}", dossier.getBirthdate(), e);
					return false;
				}
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

			// Vérification du résultat
			if (childrenAtAddress.isEmpty()) {
				logger.warn("Aucun enfant trouvé pour l'adresse : {}", address);
			} else {
				logger.info("Nombre d'enfants trouvés pour l'adresse {} : {}", address, childrenAtAddress.size());
			}

			return childrenAtAddress;

		} catch (Exception e) {
			logger.error("Erreur lors de la récupération des enfants pour l'adresse : {}", address, e);
			throw new Exception("Une erreur s'est produite lors de la récupération des enfants pour l'adresse.", e);
		}
	}

	/**
	 * Normalise l'adresse en supprimant les espaces inutiles et en la convertissant
	 * en minuscules.
	 * 
	 * @param address l'adresse à normaliser.
	 * @return l'adresse normalisée.
	 */
	private String normalizeAddress(String address) {
		if (address == null) {
			return "";
		}
		return address.trim().toLowerCase();
	}

	public Boolean isChild(String birthdate) {
		try {
			LocalDate birthDate = LocalDate.parse(birthdate, DATE_FORMATTER);
			int age = Period.between(birthDate, LocalDate.now()).getYears();
			return age < 18;
		} catch (DateTimeParseException e) {
			return false;
		}
	}

	public Map<String, Object> personInfo(String lastName) throws Exception {
	    logger.info("Entrée dans la méthode personInfo() de personService avec lastName : {}", lastName);

	    // Liste finale pour stocker les informations
	    List<Map<String, Object>> personInfo = new ArrayList<>();

	    // Récupérer toutes les personnes et dossiers médicaux
	    List<Person> personList = personRepository.getAllPerson();
	    List<Medicalrecord> medicalrecordList = medicalrecordService.getAllMedicalrecord();

	    logger.info("Voici la liste des personnes : {}", personList);

	    // Filtrer les personnes par nom de famille
	    List<Person> filteredPerson = personList.stream()
	            .filter(person -> person.getLastName().equalsIgnoreCase(lastName))
	            .collect(Collectors.toList());
	    logger.info("Voici la liste des personnes filtrées : {}", filteredPerson);

	    // Parcourir les personnes filtrées et trouver leurs dossiers médicaux
	    for (Person person : filteredPerson) {
	        medicalrecordList.stream()
	                .filter(record -> record.getFirstName().equalsIgnoreCase(person.getFirstName())
	                        && record.getLastName().equalsIgnoreCase(person.getLastName()))
	                .forEach(record -> {
	                    // Utiliser LinkedHashMap pour préserver l'ordre des champs
	                    Map<String, Object> info = new LinkedHashMap<>();
	                    info.put("firstName", person.getFirstName());
	                    info.put("lastName", person.getLastName());
	                    info.put("birthdate", record.getBirthdate());
	                    info.put("address", person.getAddress());
	                    info.put("phone", person.getPhone());
	                    info.put("medications", record.getMedications());
	                    info.put("allergies", record.getAllergies());

	                    // Ajouter à la liste finale
	                    personInfo.add(info);
	                });
	    }

	    // Ajouter le nombre de personnes trouvées
	    int count = personInfo.size();

	    // Retourner la liste des informations et le nombre
	    Map<String, Object> result = new LinkedHashMap<>();
	    result.put("count", count);
	    result.put("personInfo", personInfo);

	    return result;
	}


}

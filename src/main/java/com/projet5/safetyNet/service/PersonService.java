package com.projet5.safetyNet.service;

import com.projet5.safetyNet.model.Person;
import com.projet5.safetyNet.repository.PersonRepository;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

	private static Logger logger = LogManager.getLogger(PersonService.class);
	private final PersonRepository personRepository;

	public PersonService(PersonRepository personRepository) {
		this.personRepository = personRepository;
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
				personRepository.addPerson(updatedPerson);
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
	 * données pour récupérer toutes les personnes, puis filtre celles qui vivent dans
	 * la ville donnée. Elle retourne une liste contenant les adresses email des
	 * personnes habitant dans la ville spécifiée.
	 * </p>
	 * 
	 * @param city le nom de la ville dans laquelle rechercher les adresses email
	 *             des personnes.
	 * @return une liste contenant les adresses email des personnes vivant dans la
	 *         ville spécifiée, ou une liste vide si aucune personne n'est trouvée
	 *         pour cette ville.
	 * @throws Exception si une erreur survient lors de la récupération des données
	 *                   (par exemple, si la ville est invalide, ou une erreur technique
	 *                   survient dans la récupération des données depuis le repository).
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
	        List<String> communityEmail = personList.stream()
	                .filter(person -> person.getCity().equalsIgnoreCase(city))
	                .map(person -> person.getEmail())
	                .collect(Collectors.toList());
	        if (communityEmail.isEmpty()) {
	            logger.warn("Aucune personne trouvée pour la ville : {}", city);
	        } else {
	            logger.info("Nombre d'adresses email récupérées pour la ville {} : {}", city, communityEmail.size());
	        }

	        return communityEmail;
	        
	    } catch (Exception e) {
	        logger.error("Une erreur s'est produite lors de la récupération des adresses email pour la ville : {}", city, e);
	        throw new Exception("Une erreur s'est produite dans la récupération des données.", e);
	    }
	}

}

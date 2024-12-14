package com.projet5.safetyNet.repository;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.projet5.safetyNet.model.DataModel;
import com.projet5.safetyNet.model.Person;

@Repository
public class PersonRepository {

	private static Logger logger = LogManager.getLogger(PersonRepository.class);
	public DataRepository dataRepository;
	public List<Person> personsList;
	public DataModel dataModel;

	/**
	 * Constructeur de la classe {@link PersonRepository}.
	 * <p>
	 * Ce constructeur initialise un objet {@link PersonRepository} en utilisant un
	 * objet {@link DataRepository} pour lire les données. Il charge ensuite ces
	 * données dans un {@link DataModel}, à partir duquel la liste des personnes est
	 * extraite pour être utilisée dans la classe {@link PersonRepository}.
	 * </p>
	 * <p>
	 * La méthode {@link DataRepository#readFile()} est appelée pour lire les
	 * données et initialiser l'objet {@link DataModel}, qui contient la liste des
	 * personnes à manipuler.
	 * </p>
	 * 
	 * @param dataRepository L'objet {@link DataRepository} qui est responsable de
	 *                       la lecture des données (par exemple, depuis un fichier
	 *                       ou une base de données).
	 */
	public PersonRepository(DataRepository dataRepository) {
		this.dataRepository = dataRepository;
		this.dataModel = dataRepository.readFile();
		this.personsList = dataModel.getPersonsList();
	}

	/**
	 * Méthode pour récupérer toutes les personnes enregistrées.
	 * 
	 * @return Une liste de toutes les personnes présentes dans le fichier JSON.
	 * @throws Exception
	 */
	public List<Person> getAllPerson() throws Exception {
		logger.info("Entrée dans la méthode getAllPersons() de la class PersonRepository.");
		try {
			return personsList;
		} catch (Exception e) {
			throw new Exception("Erreur lors de la récupération de la liste de personne.", e);
		}
	}

	/**
	 * Méthode pour supprimer une personne spécifique de la liste en mémoire.
	 * <p>
	 * Cette méthode recherche et supprime la personne correspondant aux critères
	 * (prénom, nom et numéro de téléphone) dans la liste des personnes en mémoire.
	 * Si la suppression réussit, elle met à jour la liste dans le modèle de données
	 * et réécrit le fichier de données.
	 * </p>
	 * 
	 * @param firstName Prénom de la personne à supprimer.
	 * @param lastName  Nom de famille de la personne à supprimer.
	 * @param phone     Numéro de téléphone de la personne à supprimer.
	 * @throws Exception Si la personne n'existe pas dans la liste ou si une erreur
	 *                   survient lors de la suppression ou de l'écriture dans le
	 *                   fichier.
	 */
	public void deletePerson(String firstName, String lastName, String phone) throws Exception {
		logger.info("Entrée dans la méthode deletePerson() de la class PersonRepository.");
		// Suppression de la personne correspondant aux critères
		try {
			logger.info("Vérification et suppression de la personne correspondant aux critères.");
			boolean isRemoved = personsList.removeIf(person -> person.getFirstName().equalsIgnoreCase(firstName)
					&& person.getLastName().equalsIgnoreCase(lastName) && person.getPhone().equalsIgnoreCase(phone));
			if (isRemoved) {
				logger.info("La personne a été supprimée. Mise à jour de la liste.");
				dataModel.setPersonsList(personsList);
				logger.info("Mise à jour effectuée. Ecriture du document.");
				dataRepository.writeFile(dataModel);
			} else {
				logger.error("La personne n'a pas pu être supprimée.");
				throw new Exception("La personne n'a pas pu être supprimée");
			}
		} catch (Exception e) {
			logger.error("Erreur lors de la suppression de la personne.", e);
			throw new Exception("Erreur lors de la suppression de la personne.", e);
		}
	}

	/**
	 * Méthode pour ajouter une nouvelle personne à la liste.
	 * <p>
	 * Cette méthode ajoute la nouvelle personne à la liste actuelle des personnes. 
	 * Après l'ajout, la liste est mise à jour dans le modèle de données et le fichier 
	 * est réécrit pour refléter ce changement.
	 * </p>
	 * 
	 * @param newPerson Objet {@link Person} représentant la nouvelle personne à ajouter à la liste.
	 * @throws Exception Si une erreur survient lors de l'ajout de la personne ou de l'écriture dans le fichier de données.
	 */
	public void addPerson(Person newPerson) throws Exception {
	    logger.info("Entrée dans la méthode addPerson() de la class PersonRepository.");
	    try {
	        logger.info("Ajout de la nouvelle personne.");
	        personsList.add(newPerson);
	        logger.info("La personne a été ajoutée. Mise à jour de la liste.");
	        dataModel.setPersonsList(personsList);

	        logger.info("Mise à jour effectuée. Ecriture du document.");
	        dataRepository.writeFile(dataModel);
	    } catch (Exception e) {
	        logger.error("Erreur lors de l'ajout de la personne.", e);
	        throw new Exception("Erreur lors de l'ajout de la personne.", e);
	    }
	}
	
	
	
	
	
	
	

	/**
	 * Méthode pour mettre à jour les informations d'une personne existante.
	 * 
	 * @param updatedPerson La personne contenant les informations mises à jour.
	 */
	public void putPerson(Person updatedPerson) {
		// Parcourir la liste pour trouver la personne correspondante
		for (int i = 0; i < personsList.size(); i++) {
			Person person = personsList.get(i);
			// Comparer les noms et prénoms pour identifier la personne à mettre à jour
			if (person.getFirstName().equalsIgnoreCase(updatedPerson.getFirstName())
					&& person.getLastName().equalsIgnoreCase(updatedPerson.getLastName())) {
				// Remplacer l'ancienne personne par la nouvelle version mise à jour
				personsList.set(i, updatedPerson);
				break;
			}
		}

		// Mettre à jour la liste des personnes dans dataModel
		dataModel.setPersonsList(personsList);

		// Écrire les modifications dans le fichier JSON
		dataRepository.writeFile(dataModel);
	}
}

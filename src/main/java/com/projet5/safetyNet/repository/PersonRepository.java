package com.projet5.safetyNet.repository;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.projet5.safetyNet.model.DataModel;
import com.projet5.safetyNet.model.Person;

/**
 * Class Repository pour gérer les opérations liées aux personnes (person).
 * 
 * Cette classe permet d'effectuer des opérations CRUD (Create, Read, Update,
 * Delete) sur la liste des personnes stockées dans le fichier de données. Elle
 * interagit avec le DataRepository pour lire et écrire les données dans le
 * fichier JSON. Cette class permet de :
 * <ul>
 * <li>Ajouter une nouvelle personne.</li>
 * <li>Supprimer les données d'une personne existante.</li>
 * <li>Mettre à jour les informations d'une personne.</li>
 * <li>Récupérer les informations de toutes les personnes.</li>
 * </ul>
 */
@Repository
public class PersonRepository {

    /**
     * Logger pour enregistrer les informations et les erreurs liées à {@link PersonRepository}.
     */
    private static final Logger logger = LogManager.getLogger(PersonRepository.class);

    /**
     * Référence au repository global des données pour effectuer les lectures et écritures.
     */
    public DataRepository dataRepository;

    /**
     * Liste des personnes contenues dans les données.
     */
    public List<Person> personsList;

    /**
     * Modèle de données principal contenant les collections manipulées par ce repository.
     */
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
		logger.info("PersonRepository, initialisé avec succès.");
	}

	/**
	 * Récupère la liste de toutes les personnes présentes dans le dépôt.
	 * 
	 * Cette méthode renvoie la liste complète des personnes stockées dans `personsList`. 
	 * Si la liste contient des données, elles sont retournées telles quelles.
	 * Cette méthode peut être utilisée pour obtenir toutes les personnes sans filtrage.
	 * 
	 * @return Une liste contenant toutes les personnes présentes dans le dépôt.
	 */
	public List<Person> getAllPerson() {
		logger.info("Liste contenant toutes les perosnnes récupérée.");
		logger.debug("Le contenu de la liste : {}", personsList);
		return personsList;
	}

	/**
	 * Supprime une personne de la liste en fonction de son prénom, nom et numéro de téléphone.
	 * 
	 * Cette méthode recherche une personne correspondant aux critères spécifiés et la supprime de la liste. 
	 * Ensuite, la liste est mise à jour et sauvegardée dans le fichier.
	 * 
	 * @param firstName Le prénom de la personne à supprimer.
	 * @param lastName Le nom de la personne à supprimer.
	 * @param phone Le numéro de téléphone de la personne à supprimer.
	 */
	public void deletePerson(String firstName, String lastName, String phone) {
		logger.debug("Entrée dans la méthode deletePerson() de la class PersonRepository.");
		logger.debug("Vérification et suppression de la personne correspondant aux critères.");
		logger.debug("La personne prenom: {}, nom: {}, phone: {}", firstName, lastName, phone);
		personsList.removeIf(person -> person.getFirstName().equalsIgnoreCase(firstName)
				&& person.getLastName().equalsIgnoreCase(lastName) && person.getPhone().equalsIgnoreCase(phone));
		logger.debug("La personne a été supprimée. Mise à jour de la liste.");
		dataModel.setPersonsList(personsList);
		logger.info("Mise à jour effectuée. Ecriture du document.");
		dataRepository.writeFile(dataModel);
	}

	/**
	 * Ajoute une nouvelle personne à la liste des personnes et met à jour le modèle de données.
	 * 
	 * Après l'ajout de la personne, la liste est sauvegardée dans le fichier.
	 * 
	 * @param newPerson La personne à ajouter à la liste.
	 * 
	 * @see Person
	 */
	public void addPerson(Person newPerson){
	    logger.debug("Entrée dans la méthode addPerson() de la class PersonRepository.");
	    logger.debug("Ajout de la nouvelle personne.");
	    personsList.add(newPerson);
	    logger.debug("La nouvelle personne à ajoutée est : {}", newPerson);
	    logger.info("La personne a été ajoutée. Mise à jour de la liste.");
	    dataModel.setPersonsList(personsList);
	    dataRepository.writeFile(dataModel);
	}

	/**
	 * Met à jour les informations d'une personne dans la liste.
	 * 
	 * Cette méthode cherche une personne dans la liste par son prénom et nom, puis met à jour ses informations.
	 * Si la personne n'est pas trouvée, rien ne se passe. Les informations sont ensuite sauvegardées dans le fichier.
	 * 
	 * @param updatedPerson La personne avec les nouvelles informations à mettre à jour.
	 * 
	 * @see Person
	 */
	public void updatePerson(Person updatedPerson) {
		logger.debug("Entrée dans la méthode updatePerson de la class PersonRepository.");
		logger.debug("Les données à mettre à jour sont : {}", updatedPerson);

		for (int i = 0; i < personsList.size(); i++) {
			Person person = personsList.get(i);
			if (person.getFirstName().equalsIgnoreCase(updatedPerson.getFirstName())
					&& person.getLastName().equalsIgnoreCase(updatedPerson.getLastName())) {
				logger.debug("Mise à jour de la personne à l'index " + i + " : " + updatedPerson);
				personsList.set(i, updatedPerson);
				break;
			}
		}
		dataModel.setPersonsList(personsList);
		dataRepository.writeFile(dataModel);
		logger.info("Les données ont été mise à jour avec succès.");
	}

}

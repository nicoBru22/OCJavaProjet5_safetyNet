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
	}

	/**
	 * Récupère la liste de toutes les personnes.
	 * <p>
	 * Cette méthode permet de récupérer toutes les personnes enregistrées. Cette
	 * liste a été initialisée dans le constructeur.
	 * </p>
	 * 
	 * @return personList Une liste de toutes les personnes présentes dans le
	 *         fichier JSON.
	 * @throws Exception si une erreur intervient lors de la récupération des
	 *                   données.
	 */
	public List<Person> getAllPerson() throws Exception {
		logger.info("Entrée dans la méthode getAllPersons() de la class PersonRepository.");
		try {
			logger.info("Récupération de la liste contenant toutes les perosnnes.");
			logger.debug("Le contenu de la liste : {}", personsList);
			return personsList;
		} catch (Exception e) {
			logger.error("Erreur lors de la récupération de la liste des personnes.", e);
			throw new Exception("Erreur lors de la récupération de la liste de personne.", e);
		}
	}

	/**
	 * Supprime une personne du systeme.
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
		try {
			logger.info("Vérification et suppression de la personne correspondant aux critères.");
			logger.debug("La personne prenom: {}, nom: {}, phone: {}", firstName, lastName, phone);
			boolean isRemoved = personsList.removeIf(person -> person.getFirstName().equalsIgnoreCase(firstName)
					&& person.getLastName().equalsIgnoreCase(lastName) && person.getPhone().equalsIgnoreCase(phone));
			if (isRemoved) {
				logger.info("La personne a été supprimée. Mise à jour de la liste.");
				dataModel.setPersonsList(personsList);
				logger.info("Mise à jour effectuée. Ecriture du document.");
				dataRepository.writeFile(dataModel);
			} else {
				logger.error("{} {} n'a pas pu être supprimée.", firstName, lastName);
				throw new Exception("La personne n'a pas pu être supprimée");
			}
		} catch (Exception e) {
			logger.error("Erreur lors de la suppression de la personne.", e);
			throw new Exception("Erreur lors de la suppression de la personne.", e);
		}
	}

	/**
	 * Ajoute une nouvelle personne à la liste.
	 * <p>
	 * Cette méthode ajoute la nouvelle personne à la liste actuelle des personnes.
	 * Après l'ajout, la liste est mise à jour dans le modèle de données et le
	 * fichier est réécrit pour refléter ce changement.
	 * </p>
	 * 
	 * @param newPerson Objet {@link Person} représentant la nouvelle personne à
	 *                  ajouter à la liste.
	 * @throws Exception Si une erreur survient lors de l'ajout de la personne ou de
	 *                   l'écriture dans le fichier de données.
	 */
	public void addPerson(Person newPerson) throws Exception {
		logger.info("Entrée dans la méthode addPerson() de la class PersonRepository.");
		try {
			logger.info("Ajout de la nouvelle personne.");
			personsList.add(newPerson);
			logger.debug("La nouvelle personne à ajoutée est : {}", newPerson);
			logger.info("La personne a été ajoutée. Mise à jour de la liste.");
			dataModel.setPersonsList(personsList);
			dataRepository.writeFile(dataModel);
		} catch (Exception e) {
			logger.error("Erreur lors de l'ajout de la personne.", e);
			throw new Exception("Erreur lors de l'ajout de la personne.", e);
		}
	}

	/**
	 * Met à jour les informations d'une personne existante.
	 *
	 * <p>
	 * Cette méthode met à jour une personne existante dans le systeme après avoir
	 * comparé les firstName et lastName. Après la mise à jour, la liste est mise à
	 * jour dans le modèle de données et le fichier est réécrit pour refléter ce
	 * changement.
	 * </p>
	 * 
	 * @param updatedPerson Objet {@link Person} représentant les informations mises
	 *                      à jour.
	 * @throws Exception Si la personne n'existe pas ou si une erreur survient lors
	 *                   de l'écriture.
	 */
	public void updatePerson(Person updatedPerson) throws Exception {
		logger.info("Entrée dans la méthode updatePerson de la class PersonRepository.");
		logger.debug("Les données à mettre à jour sont : {}", updatedPerson);
		if (updatedPerson == null || updatedPerson.getFirstName() == null || updatedPerson.getLastName() == null) {
			throw new IllegalArgumentException("Les informations de la personne à mettre à jour sont invalides.");
		}

		boolean isUpdated = false;
		try {
			for (int i = 0; i < personsList.size(); i++) {
				Person person = personsList.get(i);
				if (person.getFirstName().equalsIgnoreCase(updatedPerson.getFirstName())
						&& person.getLastName().equalsIgnoreCase(updatedPerson.getLastName())) {
					logger.info("Mise à jour de la personne à l'index " + i + " : " + updatedPerson);
					personsList.set(i, updatedPerson);
					isUpdated = true;
					break;
				}
			}

			if (!isUpdated) {
				logger.error("Aucune personne trouvée avec les informations fournies : {} ", updatedPerson);
				throw new Exception("La personne à mettre à jour n'existe pas.");
			}
			dataModel.setPersonsList(personsList);
			dataRepository.writeFile(dataModel);
			logger.info("Les données ont été mise à jour avec succès.");

		} catch (Exception e) {
			logger.error("Erreur lors de la mise à jour de la personne.", e);
			throw new Exception("Erreur lors de la mise à jour de la personne.", e);
		}
	}

}

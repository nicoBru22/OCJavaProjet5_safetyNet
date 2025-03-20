package com.projet5.safetyNet.repository;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import com.projet5.safetyNet.model.DataModel;
import com.projet5.safetyNet.model.Firestation;

/**
 * Class Repository pour gérer les opérations liées aux casernes de pompiers
 * (firestation).
 * <p>
 * Cette classe permet d'effectuer des opérations CRUD (Create, Read, Update,
 * Delete) sur la liste des casernes de pompiers stockées dans le fichier de
 * données. Elle interagit avec le DataRepository pour lire et écrire les
 * données dans le fichier JSON.
 * </p>
 * 
 * Cette classe permet de :
 * <ul>
 * <li>Ajouter une nouvelle caserne.</li>
 * <li>Supprimer une caserne existante.</li>
 * <li>Mettre à jour les informations d'une caserne.</li>
 * <li>Récupérer toutes les casernes.</li>
 * </ul>
 */
@Repository
public class FirestationRepository {

	private static final Logger logger = LogManager.getLogger(FirestationRepository.class);

	private final DataRepository dataRepository;
	private final DataModel dataModel;
	private List<Firestation> firestationList;

	/**
	 * Constructeur de la classe {@link FirestationRepository}
	 * <p>
	 * Ce constructeur initialise les données à partir du fichier JSON via le
	 * DataRepository.
	 * </p>
	 * <p>
	 * La méthode {@link DataRepository#readFile()} est appelée pour lire les
	 * données et initialiser l'objet {@link DataModel}, qui contient la liste des
	 * casernes à manipuler.
	 * </p>
	 * 
	 * @param dataRepository le repository utilisé pour lire et écrire les données.
	 *                       Il est injecté au moment de l'instanciation de la
	 *                       classe.
	 */
	public FirestationRepository(DataRepository dataRepository) {
		this.dataRepository = dataRepository;
		this.dataModel = dataRepository.readFile();
		this.firestationList = dataModel.getFireStations();
		logger.info("FirestationRepository initialisé avec succès.");
	}


	/**
	 * Récupère la liste de toutes les casernes.
	 *
	 * Cette méthode retourne une liste contenant toutes les casernes enregistrées dans le système.
	 *
	 * @return une liste de toutes les casernes
	 */
	public List<Firestation> getAllFirestations() {
		logger.info("Liste de toutes les casernes récupérée.");
		logger.debug("Le contenu de la liste : {}", firestationList);
		return firestationList;
	}


	/**
	 * Ajoute une nouvelle caserne.
	 *
	 * Cette méthode ajoute une nouvelle caserne à la liste des casernes. Si la caserne existe déjà, une exception est lancée.
	 *
	 * @param newFirestation la nouvelle caserne à ajouter
	 */
	public void addFirestation(Firestation newFirestation) {
		firestationList.add(newFirestation);
		dataModel.setFireStations(firestationList);
		dataRepository.writeFile(dataModel);
		logger.info("Nouvelle caserne ajoutée.");
		logger.debug("La nouvelle caserne ajoutée : " + newFirestation);
	}


	/**
	 * Supprime une caserne.
	 *
	 * Cette méthode supprime une caserne de la liste en fonction de son adresse et de son nom de station. Si la caserne n'est pas trouvée,
	 * une exception est lancée.
	 *
	 * @param deletedFirestation la caserne à supprimer
	 */
	public void deleteFirestation(Firestation deletedFirestation) {
		firestationList.removeIf(firestation -> firestation.getAddress().equals(deletedFirestation.getAddress())
				&& firestation.getStation().equals(deletedFirestation.getStation()));
		dataModel.setFireStations(firestationList);
		dataRepository.writeFile(dataModel);
		logger.info("Caserne supprimée avec succès ");
		logger.debug("Caserne supprimée : {}", deletedFirestation);
	}


	/**
	 * Met à jour une caserne existante.
	 *
	 * Cette méthode met à jour une caserne en fonction de son adresse. Si la caserne n'est pas trouvée, une exception est lancée.
	 *
	 * @param updatedFirestation la caserne mise à jour
	 */
	public void updateFirestation(Firestation updatedFirestation) {
	    Optional<Firestation> firestationOptional = firestationList.stream()
	            .filter(firestation -> firestation.getAddress().equalsIgnoreCase(updatedFirestation.getAddress()))
	            .findFirst();
        Firestation existingFirestation = firestationOptional.get();
        firestationList.set(firestationList.indexOf(existingFirestation), updatedFirestation);
        dataModel.setFireStations(firestationList);
        dataRepository.writeFile(dataModel);
        logger.info("Caserne mise à jour avec succès : {}", updatedFirestation);
	}
}

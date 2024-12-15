package com.projet5.safetyNet.repository;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.stereotype.Repository;

import com.projet5.safetyNet.model.DataModel;
import com.projet5.safetyNet.model.Firestation;

/**
 * Repository pour gérer les opérations liées aux casernes de pompiers. Cette
 * <p>
 * classe permet d'effectuer des opérations CRUD (Create, Read, Update, Delete)
 * sur la liste des casernes de pompiers stockées dans le fichier de données.
 * Elle interagit avec le DataRepository pour lire et écrire les données dans le
 * fichier JSON.
 * </p>
 * 
 * Voici les principales fonctionnalités de la classe :
 * <ul>
 * <li>Ajouter une nouvelle caserne.</li>
 * <li>Supprimer une caserne existante.</li>
 * <li>Mettre à jour les informations d'une caserne.</li>
 * <li>Récupérer toutes les casernes.</li>
 * </ul>
 * </p>
 */
@Repository
public class FirestationRepository {

	private static final Logger LOGGER = Logger.getLogger(FirestationRepository.class.getName());

	private final DataRepository dataRepository;
	private final DataModel dataModel;
	private List<Firestation> firestationList;

	/**
	 * Constructeur de la classe FirestationRepository. Initialise les données à
	 * partir du fichier JSON via le DataRepository.
	 * 
	 * @param dataRepository le repository utilisé pour lire et écrire les données.
	 *                       Il est injecté au moment de l'instanciation de la
	 *                       classe.
	 */
	public FirestationRepository(DataRepository dataRepository) {
		this.dataRepository = dataRepository;
		this.dataModel = dataRepository.readFile();
		this.firestationList = dataModel.getFireStations();
		LOGGER.info("FirestationRepository initialisé avec succès.");
	}

	/**
	 * Récupère la liste de toutes les casernes de pompiers.
	 * <p>
	 * Cette méthode permet de récupérer la liste des casernes à partir de l'objet
	 * DataModel.
	 * </p>
	 * 
	 * @return Liste des casernes de pompiers.
	 * @throws Exception si une erreur survient lors de la récupération des données.
	 */
	public List<Firestation> getAllFirestations() throws Exception {
		try {
			LOGGER.info("Récupération de toutes les casernes.");
			return firestationList;
		} catch (Exception e) {
			LOGGER.severe("Erreur lors de la récupération des casernes : " + e.getMessage());
			throw new Exception("Erreur : ", e);
		}
	}

	/**
	 * Ajoute une nouvelle caserne
	 * <p>
	 * Ajoute une nouvelle caserne à la liste des casernes et met à jour le fichier
	 * de données. Cette méthode permet d'ajouter une caserne à la liste interne des
	 * casernes, puis de enregistrer cette nouvelle liste dans le fichier JSON en
	 * appelant la méthode writeFile de DataRepository.
	 * </p>
	 * 
	 * @param newFirestation la nouvelle caserne à ajouter.
	 */
	public void addFirestation(Firestation newFirestation) {
		firestationList.add(newFirestation);
		dataModel.setFireStations(firestationList);
		dataRepository.writeFile(dataModel);
		LOGGER.info("Nouvelle caserne ajoutée : " + newFirestation.getStation());
	}

	/**
	 * Supprime une caserne
	 * <p>
	 * Supprime une caserne de la liste des casernes et met à jour le fichier de
	 * données. Cette méthode permet de supprimer une caserne spécifique en se
	 * basant sur son adresse et son numéro de station. Une fois supprimée, la liste
	 * des casernes est enregistrée à nouveau dans le fichier JSON.
	 * </p>
	 * 
	 * @param deletedFirestation la caserne à supprimer.
	 */
	public void deleteFirestation(Firestation deletedFirestation) {
		firestationList.removeIf(firestation -> firestation.getAddress().equals(deletedFirestation.getAddress())
				&& firestation.getStation().equals(deletedFirestation.getStation()));
		dataModel.setFireStations(firestationList);
		dataRepository.writeFile(dataModel);
		LOGGER.info("Caserne supprimée : " + deletedFirestation.getStation());
	}

	/**
	 * Met à jour les informations d'une caserne
	 * <p>
	 * Met à jour les informations d'une caserne existante dans la liste des
	 * casernes. Cette méthode recherche une caserne dans la liste en fonction de
	 * son adresse. Si la caserne est trouvée, elle est mise à jour avec les
	 * nouvelles informations. Si la caserne n'existe pas, une exception est lancée.
	 * </p>
	 * 
	 * @param updatedFirestation la caserne mise à jour.
	 * @throws IllegalArgumentException si la caserne à mettre à jour n'est pas
	 *                                  trouvée.
	 */
	public void updateFirestation(Firestation updatedFirestation) {
		boolean updated = firestationList.stream()
				.filter(firestation -> firestation.getAddress().equalsIgnoreCase(updatedFirestation.getAddress()))
				.findFirst().map(firestation -> {
					firestationList.set(firestationList.indexOf(firestation), updatedFirestation);
					return true;
				}).orElse(false);

		if (updated) {
			dataModel.setFireStations(firestationList);
			dataRepository.writeFile(dataModel);
			LOGGER.info("Caserne mise à jour : " + updatedFirestation.getStation());
		} else {
			LOGGER.warning("Caserne non trouvée pour mise à jour : " + updatedFirestation.getStation());
			throw new IllegalArgumentException("Caserne non trouvée pour mise à jour.");
		}
	}
}

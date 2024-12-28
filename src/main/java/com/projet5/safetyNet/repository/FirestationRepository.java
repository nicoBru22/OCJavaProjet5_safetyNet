package com.projet5.safetyNet.repository;

import java.util.List;

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
	 * Récupère la liste de toutes les casernes de pompiers.
	 * <p>
	 * Cette méthode permet de récupérer la liste des casernes. Cette liste a été
	 * initialisée dans le constructeur.
	 * </p>
	 * 
	 * @return firestationList La liste de toutes les casernes de pompiers.
	 * @throws Exception si une erreur survient lors de la récupération des données.
	 */
	public List<Firestation> getAllFirestations() throws Exception {
		try {
			logger.info("Récupération de toutes les casernes.");
			logger.debug("Le contenu de la liste : {}", firestationList);
			return firestationList;
		} catch (Exception e) {
			logger.error("Erreur lors de la récupération des casernes : " + e.getMessage());
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
	 * @param newFirestation Object {@link Firestation} contenant la nouvelle
	 *                       caserne à ajouter.
	 * @throws Exception si une erreur survient lors de l'ajout des données.
	 */
	public void addFirestation(Firestation newFirestation) throws Exception {
		try {
			firestationList.add(newFirestation);
			dataModel.setFireStations(firestationList);
			dataRepository.writeFile(dataModel);
			logger.info("Nouvelle caserne ajoutée.");
			logger.debug("La nouvelle caserne ajoutée : " + newFirestation);
		} catch (Exception e) {
			logger.error("Erreur lors de l'ajout de la nouvelle caserne.");
			throw new Exception("Erreur lors de l'ajout de la nouvelle caserne.", e);
		}

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
	 * @param deletedFirestation Objet {@link Firestation} contenant la caserne à
	 *                           supprimer.
	 * @throws RuntimeException si une erreur survient lors de la
	 *                                  suppression des données.
	 */
	public void deleteFirestation(Firestation deletedFirestation) throws RuntimeException {
		try {
			firestationList.removeIf(firestation -> firestation.getAddress().equals(deletedFirestation.getAddress())
					&& firestation.getStation().equals(deletedFirestation.getStation()));
			dataModel.setFireStations(firestationList);
			dataRepository.writeFile(dataModel);
			logger.info("Caserne supprimée avec succès ");
			logger.debug("Caserne supprimée : {}", deletedFirestation);
		} catch (RuntimeException e) {
			logger.error("Erreur lors de la suppression des données.", e);
			throw new RuntimeException("Erreur lors de la suppression des données.", e);
		}

	}

	/**
	 * Met à jour les informations d'une caserne.
	 * <p>
	 * Cette méthode met à jour les informations d'une caserne existante dans la
	 * liste des casernes. Cette méthode recherche une caserne dans la liste en
	 * fonction de son adresse. Si la caserne est trouvée, elle est mise à jour avec
	 * les nouvelles informations. Si la caserne n'existe pas, une exception est
	 * lancée.
	 * </p>
	 * 
	 * @param updatedFirestation Objet {@link Firestation} contenant la caserne mise
	 *                           à jour.
	 * @throws Exception si une erreur intervient lors de la mise à jour de la caserne.
	 * @throws IllegalArgumentException si la caserne à mettre à jour n'est pas
	 *                                  trouvée.
	 */
	public void updateFirestation(Firestation updatedFirestation) throws Exception {
		try {
			boolean updated = firestationList.stream()
					.filter(firestation -> firestation.getAddress().equalsIgnoreCase(updatedFirestation.getAddress()))
					.findFirst().map(firestation -> {
						firestationList.set(firestationList.indexOf(firestation), updatedFirestation);
						return true;
					}).orElse(false);
			logger.debug("La contenu de la mise à jour de la caserne : {}.", updatedFirestation);
			if (updated) {
				dataModel.setFireStations(firestationList);
				dataRepository.writeFile(dataModel);
				logger.info("Caserne mise à jour avec succès.");
			} else {
				logger.error("Caserne non trouvée pour mise à jour : {}.", updatedFirestation);
				throw new IllegalArgumentException("Caserne non trouvée pour mise à jour.");
			}
		} catch (Exception e) {
			logger.error("Erreur lors de la mise à jour de la caserne : {}", updatedFirestation);
			throw new Exception("Erreur lors de la mise à jour de la caserne.", e);
		}

	}
}

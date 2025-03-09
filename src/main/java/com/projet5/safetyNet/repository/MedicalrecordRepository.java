package com.projet5.safetyNet.repository;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.projet5.safetyNet.Exception.MedicalrecordAdditionException;
import com.projet5.safetyNet.Exception.MedicalrecordDeletionException;
import com.projet5.safetyNet.Exception.MedicalrecordNotFoundException;
import com.projet5.safetyNet.model.DataModel;
import com.projet5.safetyNet.model.Medicalrecord;

/**
 * Class Repository pour gérer les opérations liées aux dossiers médicaux
 * (medicalrecord).
 * 
 * Cette classe permet d'effectuer des opérations CRUD (Create, Read, Update,
 * Delete) sur la liste des dossiers médicaux stockée dans le fichier de
 * données. Elle interagit avec le DataRepository pour lire et écrire les
 * données dans le fichier JSON. Cette classe permet de :
 * <ul>
 * <li>Ajouter un nouveau dossier médical.</li>
 * <li>Supprimer les données d'un dossier médical existant.</li>
 * <li>Mettre à jour les informations d'un dossier médical.</li>
 * <li>Récupérer les informations de tous les dossiers médicaux.</li>
 * </ul>
 */

@Repository
public class MedicalrecordRepository {

	private Logger logger = LogManager.getLogger(MedicalrecordRepository.class);

	/**
	 * Représente le référentiel des données permettant d'accéder et de manipuler les informations liées aux dossiers médicaux.
	 * Ce champ est utilisé pour interagir avec la source de données et effectuer des opérations CRUD (Create, Read, Update, Delete)
	 * sur les dossiers médicaux.
	 */
	public DataRepository dataRepository;

	/**
	 * Liste des dossiers médicaux associés. Cette liste contient des objets de type {@link Medicalrecord} et est utilisée
	 * pour stocker et gérer les informations relatives aux dossiers médicaux des personnes.
	 */
	public List<Medicalrecord> medicalrecordList;

	/**
	 * Modèle de données représentant la structure globale de l'application, incluant des informations de configuration
	 * et d'autres éléments associés au traitement des dossiers médicaux. Ce champ permet d'organiser les données et
	 * de faciliter l'accès à différentes parties de l'application.
	 */
	public DataModel dataModel;


	/**
	 * Constructeur de la classe {@link MedicalrecordRepository}.
	 * <p>
	 * Ce constructeur initialise un objet {@link MedicalrecordRepository} en
	 * utilisant un objet {@link DataRepository} pour lire les données. Il charge
	 * ensuite ces données dans un {@link DataModel}, à partir duquel la liste des
	 * personnes est extraite pour être utilisée dans la classe
	 * {@link MedicalrecordRepository}.
	 * </p>
	 * <p>
	 * La méthode {@link DataRepository#readFile()} est appelée pour lire les
	 * données et initialiser l'objet {@link DataModel}, qui contient la liste des
	 * dossiers médicaux à manipuler.
	 * </p>
	 * 
	 * @param dataRepository L'objet {@link DataRepository} qui est responsable de
	 *                       la lecture des données (par exemple, depuis un fichier
	 *                       ou une base de données).
	 */
	public MedicalrecordRepository(DataRepository dataRepository) {
		this.dataRepository = dataRepository;
		this.dataModel = dataRepository.readFile();
		this.medicalrecordList = dataModel.getMedicalrecords();
	}

	/**
	 * Récupère la liste de tous les dossiers médicaux.
	 * 
	 * <p>
	 * Cette méthode permet de récupérer l'ensemble des dossiers médicaux sous forme
	 * de liste. La liste a été initialisée dans le constructeur.
	 * </p>
	 * 
	 * @return medicalrecordList Une liste de dossier médicaux stocké dans le
	 *         systeme.
	 * @throws Exception si une erreur intervient lors de la récupération de la
	 *                   liste de dossier médicaux.
	 */
	public List<Medicalrecord> getAllMedicalrecord() {
		logger.info("Récupération de la liste de dossier médicaux.");
		logger.debug("La liste récupérée : {}", medicalrecordList);
		return medicalrecordList;

	}

	/**
	 * Ajoute un nouveau dossier médical.
	 * 
	 * <p>
	 * Cette méthode permet d'ajouter un nouveau dossier médical à la liste
	 * existante.
	 * </p>
	 * 
	 * @param newMedicalrecord Objet {@link Medicalrecord} représentant le nouveau
	 *                         dossier médical à ajouter.
	 * @throws Exception si une erreur intervient lors de l'ajout d'un nouveau
	 *                   dossier médical.
	 * 
	 */
	public void addMedicalrecord(Medicalrecord newMedicalrecord) {
		logger.debug("Tentative d'ajout du dossier médical : {}", newMedicalrecord);
		boolean medicalrecordAdded = medicalrecordList.add(newMedicalrecord);
		if(!medicalrecordAdded) {
			logger.error("Le Medicalrecord n'a pas pu être ajouté : ", newMedicalrecord);
			throw new MedicalrecordAdditionException("Le Medicalrecord n'a pas pu être ajouté : "+ newMedicalrecord);
		}
		logger.info("Le dossier médical a été ajouté avec succès.");
		dataModel.setMedicalrecords(medicalrecordList);
		dataRepository.writeFile(dataModel);

	}

	/**
	 * Supprime un dossier médical.
	 * 
	 * <p>
	 * Cette méthode permet de supprimer un dossier médical à partir du nom et du
	 * prénom de la personne à qui appartient ce dossier médical. Si le nom et le
	 * prénom correspondent à un dossier dans le systeme alors celui-ci est supprimé
	 * puis la liste est mise à jour.
	 * </p>
	 * 
	 * @param deletedMedicalrecord objet {@link Medicalrecord} représentant le
	 *                             dossier médical à supprimer.
	 * @throws Exception si une erreur intervient lors de la suppression du dossier
	 *                   médical ou si le dossier médical n'existe pas.
	 */
	public void deleteMedicalrecord(Medicalrecord deletedMedicalrecord) {
		logger.debug("Le dossier médical à supprimer : {}", deletedMedicalrecord);

		boolean isRemoved = medicalrecordList.removeIf(
				medicalrecord -> medicalrecord.getFirstName().equalsIgnoreCase(deletedMedicalrecord.getFirstName())
						&& medicalrecord.getLastName().equalsIgnoreCase(deletedMedicalrecord.getLastName()));
		if (!isRemoved) {
			logger.error("Le dossier médical de : {} {} n'a pas pu être supprimé.", deletedMedicalrecord.getFirstName(),
					deletedMedicalrecord.getLastName());
			throw new MedicalrecordDeletionException("Le Medicalrecord n'a pas pu être supprimé : " + deletedMedicalrecord);
		}
		dataModel.setMedicalrecords(medicalrecordList);
		dataRepository.writeFile(dataModel);
		logger.info("Le dossier médical a été supprimé avec succès.");

	}

	/**
	 * Met à jour un dossier médical.
	 * 
	 * <p>
	 * Cette méthode permet de mettre à jour un dossier médical en verifiant s'il
	 * correspond bien a un dossier médical stocké dans le systeme à partir de son
	 * nom et son prénom. S'il existe alors il est supprimé et la liste est mise à
	 * jour.
	 * </p>
	 * 
	 * @param updatedMedicalrecord objet {@link Medicalrecord} représentant les
	 *                             données mises à jour.
	 * @throws Exception si une erreur intervient lors de la mise à jour du dossier
	 *                   médical ou si le dossier médical n'existe pas.
	 */
	public void updateMedicalrecord(Medicalrecord updatedMedicalrecord) {
		logger.debug("Tentative de mise à jour du dossier médical : {}", updatedMedicalrecord);
		boolean updated = medicalrecordList.stream()
				.filter(medicalrecord -> medicalrecord.getFirstName().equals(updatedMedicalrecord.getFirstName())
						&& medicalrecord.getLastName().equals(updatedMedicalrecord.getLastName()))
				.findFirst().map(medicalrecord -> {
					medicalrecordList.set(medicalrecordList.indexOf(medicalrecord), updatedMedicalrecord);
					return true;
				}).orElse(false);

		if (!updated) {
			logger.error("Le dossier médical de {} {} n'a pas été trouvée", updatedMedicalrecord.getFirstName(),
					updatedMedicalrecord.getLastName());
			throw new MedicalrecordNotFoundException("Le dossier médical n'a pas été trouvée");
		}
		dataModel.setMedicalrecords(medicalrecordList);
		dataRepository.writeFile(dataModel);
		logger.info("Le dossier médical a été mis à jour.");
	}

}

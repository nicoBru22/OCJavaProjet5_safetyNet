package com.projet5.safetyNet.repository;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

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

	public DataRepository dataRepository;
	public List<Medicalrecord> medicalrecordList;
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
	public List<Medicalrecord> getAllMedicalrecord() throws Exception {
		try {
			logger.info("Récupération de la liste de dossier médicaux.");
			logger.debug("La liste récupérée : {}", medicalrecordList);
			return medicalrecordList;
		} catch (Exception e) {
			logger.error("Erreur lors de la récupération des dossiers médicaux.", e);
			throw new Exception("Erreur lors de la récupération des dossiers médicaux.", e);
		}

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
	public void addMedicalrecord(Medicalrecord newMedicalrecord) throws Exception {
		try {
			logger.info("Ajout du nouveau dossier médical.");
			logger.debug("Le dossier médical à ajouter : {}", newMedicalrecord);
			medicalrecordList.add(newMedicalrecord);
			logger.info("Le dossier médical a été ajouté avec succès.");
			dataModel.setMedicalrecords(medicalrecordList);
			dataRepository.writeFile(dataModel);
		} catch (Exception e) {
			logger.error("Erreur lors de l'ajout du dossier médical.", e);
			throw new Exception("Erreur lors de l'ajout du dossier médical.", e);
		}

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
	public void deleteMedicalrecord(Medicalrecord deletedMedicalrecord) throws Exception {
		try {
			logger.debug("Le dossier médical à supprimer : {}", deletedMedicalrecord);

			boolean isRemoved = medicalrecordList.removeIf(
					medicalrecord -> medicalrecord.getFirstName().equalsIgnoreCase(deletedMedicalrecord.getFirstName())
							&& medicalrecord.getLastName().equalsIgnoreCase(deletedMedicalrecord.getLastName()));
			if (isRemoved) {
				dataModel.setMedicalrecords(medicalrecordList);
				dataRepository.writeFile(dataModel);
				logger.info("Le dossier médical a été supprimé avec succès.");
			} else {
				logger.error("Le dossier médical de : {} {} n'existe pas.", deletedMedicalrecord.getFirstName(),
						deletedMedicalrecord.getLastName());
				throw new Exception("Le dossier médical n'existe pas.");

			}
		} catch (Exception e) {
			logger.error("Erreur lors de la suppression du dossier médical.", e);
			throw new Exception("Erreur lors de la suppression du dossier médical.", e);
		}

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
	public void updateMedicalrecord(Medicalrecord updatedMedicalrecord) throws Exception {
		try {
			logger.info("Mise à jour du dossier médical.");
			logger.debug("Le dossier médical à mettre à jour : {}", updatedMedicalrecord);
			boolean updated = medicalrecordList.stream()
					.filter(medicalrecord -> medicalrecord.getFirstName().equals(updatedMedicalrecord.getFirstName())
							&& medicalrecord.getLastName().equals(updatedMedicalrecord.getLastName()))
					.findFirst().map(medicalrecord -> {
						medicalrecordList.set(medicalrecordList.indexOf(medicalrecord), updatedMedicalrecord);
						return true;
					}).orElse(false);

			if (updated) {
				dataModel.setMedicalrecords(medicalrecordList);
				dataRepository.writeFile(dataModel);
				logger.info("Le dossier médical a été mis à jour.");
			} else {
				logger.error("Le dossier médical de {} {} n'a pas été trouvée", updatedMedicalrecord.getFirstName(),
						updatedMedicalrecord.getLastName());
				throw new Exception("Le dossier médical n'a pas été trouvée");
			}
		} catch (Exception e) {
			logger.error("Erreur lors de la mise à jour du dossier médical.", e);
			throw new Exception("Erreur lors de la mise à jour du dossier médical", e);
		}

	}

}

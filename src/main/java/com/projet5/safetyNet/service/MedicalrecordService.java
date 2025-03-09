package com.projet5.safetyNet.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.projet5.safetyNet.Exception.InvalidRequestException;
import com.projet5.safetyNet.Exception.MedicalrecordNotFoundException;
import com.projet5.safetyNet.model.Medicalrecord;
import com.projet5.safetyNet.repository.MedicalrecordRepository;

/**
 * Service pour gérer les opérations liées aux dossiers médicaux.
 * 
 * <p>
 * Cette classe permet d'effectuer des opérations CRUD (Create, Read, Update,
 * Delete) sur la liste des casernes de pompiers stockées dans le fichier de
 * données. Elle interagit avec les repositories pour effectuer les opérations
 * nécessaires de gestion des dossiers médicaux.
 * </p>
 * 
 * Voici les principales fonctionnalités de la classe :
 * <ul>
 * <li>Récupérer tous les dossiers médicaux.</li>
 * <li>Ajouter un nouveau dossier médical.</li>
 * <li>Supprimer un dossier médical.</li>
 * <li>Mettre à jour les informations d'un dossier médical.</li>
 * </ul>
 */

@Service
public class MedicalrecordService {

	private Logger logger = LogManager.getLogger(MedicalrecordService.class);
	private final MedicalrecordRepository medicalrecordRepository;

	/**
	 * Constructeur du service pour initialiser le repository nécessaire.
	 * 
	 * @param medicalrecordRepository Le repository pour les dossiers médicaux.
	 */
	public MedicalrecordService(MedicalrecordRepository medicalrecordRepository) {
		this.medicalrecordRepository = medicalrecordRepository;
	}

	/**
	 * Récupère la liste des dossiers médicaux.
	 * 
	 * <p>
	 * Cette méthode permet de récupérer la liste de tous les dossiers médicaux.
	 * Elle vérifie si la liste est vide et, si c'est le cas, renvoie une liste
	 * vide. En cas de succès, la liste des dossiers médicaux est retournée.
	 * </p>
	 * 
	 * @return Une liste contenant les objets {@link Medicalrecord}. Si aucun
	 *         dossier médical n'est trouvé, une liste vide est retournée.
	 * @throws RuntimeException Si une erreur se produit lors de la récupération des
	 *                          dossiers médicaux depuis le repository.
	 */
	public List<Medicalrecord> getAllMedicalrecord() {
		logger.info("Récupération de la liste des dossiers médicaux.");
		List<Medicalrecord> medicalrecords = medicalrecordRepository.getAllMedicalrecord();
		if (medicalrecords.isEmpty()) {
			logger.warn("La liste de dossiers médicaux est vide.");
		}
		logger.info("{} dossiers médicaux récupérés.", medicalrecords.size());
		return medicalrecords;
	}

	/**
	 * Ajoute un nouveau dossier médical.
	 * 
	 * <p>
	 * Cette méthode permet d'ajouter un nouveau dossier médical à partir des
	 * données passées en paramètre. Elle vérifie que les champs obligatoires
	 * (prénom, nom et date de naissance) ne sont ni nuls ni vides avant de procéder
	 * à la vérification de la présence du dossier dans le systeme avant de pouvoir
	 * l'ajouter.
	 * </p>
	 * 
	 * @param newMedicalrecord L'objet {@link Medicalrecord} à ajouter.
	 * @throws IllegalArgumentException Si les champs prénom, nom ou date de
	 *                                  naissance sont invalides.
	 * @throws RuntimeException         Si une erreur se produit lors de l'ajout du
	 *                                  dossier médical.
	 * @throws Exception                si un dossier médical existe déjà pour la
	 *                                  personne donnée.
	 */
	public void addMedicalrecord(Medicalrecord newMedicalrecord) {
		if (newMedicalrecord.getFirstName() == null || newMedicalrecord.getFirstName().isEmpty()
				|| newMedicalrecord.getLastName() == null || newMedicalrecord.getLastName().isEmpty()
				|| newMedicalrecord.getBirthdate() == null) {
			logger.error("Les champs firstName, lastName ou birthdate sont manquants ou invalides.");
			throw new InvalidRequestException("Les champs prénom, nom et date de naissance sont obligatoires.");
		}

		List<Medicalrecord> medicalrecordList = medicalrecordRepository.getAllMedicalrecord();
		boolean isExist = medicalrecordList.stream().anyMatch(
				medicalrecord -> medicalrecord.getFirstName().equalsIgnoreCase(newMedicalrecord.getFirstName())
						&& medicalrecord.getLastName().equalsIgnoreCase(newMedicalrecord.getLastName())
						&& medicalrecord.getBirthdate().equalsIgnoreCase(newMedicalrecord.getBirthdate()));

		if (isExist) {
			logger.error("Un dossier médical existe déjà pour {} {} {}", newMedicalrecord.getFirstName(),
					newMedicalrecord.getLastName(), newMedicalrecord.getBirthdate());
			throw new MedicalrecordNotFoundException("Un dossier médical existe déjà pour cette personne.");
		}

		logger.info("Ajout d'un nouveau dossier médical pour {} {}.", newMedicalrecord.getFirstName(),
				newMedicalrecord.getLastName());
		medicalrecordRepository.addMedicalrecord(newMedicalrecord);
		logger.info("Le dossier médical de {} {} a été ajouté avec succès.", newMedicalrecord.getFirstName(),
				newMedicalrecord.getLastName());
	}

	/**
	 * Supprime un dossier médical existant.
	 * 
	 * <p>
	 * Cette méthode permet de supprimer un dossier médical existant à partir des
	 * informations contenues dans le paramètre. Elle vérifie que les champs prénom
	 * et nom ne sont pas nuls et que le dossier médical existe avant de procéder à
	 * sa suppression.
	 * </p>
	 * 
	 * @param deletedMedicalrecord L'objet {@link Medicalrecord} à supprimer.
	 * @throws IllegalArgumentException Si les champs prénom ou nom sont invalides.
	 * @throws RuntimeException         Si une erreur se produit lors de la
	 *                                  suppression du dossier médical.
	 * @throws Exception                si le dossier médical n'existe pas pour la
	 *                                  personne donnée.
	 */
	public void deleteMedicalrecord(Medicalrecord deletedMedicalrecord) {
		if (deletedMedicalrecord.getFirstName() == null || deletedMedicalrecord.getFirstName().isEmpty()
				|| deletedMedicalrecord.getLastName() == null || deletedMedicalrecord.getLastName().isEmpty()) {
			logger.error("Les champs firstName ou lastName sont invalides pour : {} {}",
					deletedMedicalrecord.getFirstName(), deletedMedicalrecord.getLastName());
			throw new InvalidRequestException("Les champs prénom et nom sont obligatoires.");
		}

		List<Medicalrecord> medicalrecordList = medicalrecordRepository.getAllMedicalrecord();
		boolean isExist = medicalrecordList.stream().anyMatch(
				medicalrecord -> medicalrecord.getFirstName().equalsIgnoreCase(deletedMedicalrecord.getFirstName())
						&& medicalrecord.getLastName().equalsIgnoreCase(deletedMedicalrecord.getLastName()));

		if (!isExist) {
			logger.error("Le dossier médical n'existe pas pour {} {}.", deletedMedicalrecord.getFirstName(),
					deletedMedicalrecord.getLastName());
			throw new MedicalrecordNotFoundException("Le dossier médical n'existe pas pour cette personne.");
		}

		logger.info("Suppression du dossier médical pour {} {}.", deletedMedicalrecord.getFirstName(),
				deletedMedicalrecord.getLastName());
		medicalrecordRepository.deleteMedicalrecord(deletedMedicalrecord);
		logger.info("Le dossier médical de {} {} a été supprimé avec succès.", deletedMedicalrecord.getFirstName(),
				deletedMedicalrecord.getLastName());
	}

	/**
	 * Met à jour un dossier médical existant.
	 * 
	 * <p>
	 * Cette méthode permet de mettre à jour un dossier médical existant à partir
	 * des données fournies en paramètre. Elle vérifie si les champs prénom et nom
	 * ne sont pas nuls, et si le dossier médical existe en base de données avant de
	 * procéder à sa mise à jour.
	 * </p>
	 * 
	 * @param updatedMedicalrecord L'objet {@link Medicalrecord} contenant les
	 *                             nouvelles informations.
	 * @throws IllegalArgumentException Si les champs prénom ou nom sont invalides.
	 * @throws RuntimeException         Si une erreur se produit lors de la mise à
	 *                                  jour du dossier médical.
	 * @throws Exception                si le dossier médical n'existe pas pour la
	 *                                  personne donnée.
	 */
	public void updateMedicalrecord(Medicalrecord updatedMedicalrecord) {
		if (updatedMedicalrecord.getFirstName() == null || updatedMedicalrecord.getFirstName().isEmpty()
				|| updatedMedicalrecord.getLastName() == null || updatedMedicalrecord.getLastName().isEmpty()) {
			logger.error("Les champs firstName ou lastName sont vides ou invalides pour : {} {}",
					updatedMedicalrecord.getFirstName(), updatedMedicalrecord.getLastName());
			throw new InvalidRequestException("Les champs prénom et nom sont obligatoires.");
		}
		List<Medicalrecord> medicalrecordList = medicalrecordRepository.getAllMedicalrecord();
		boolean isExist = medicalrecordList.stream().anyMatch(
				medicalrecord -> medicalrecord.getFirstName().equalsIgnoreCase(updatedMedicalrecord.getFirstName())
						&& medicalrecord.getLastName().equalsIgnoreCase(updatedMedicalrecord.getLastName()));

		if (!isExist) {
			logger.error("Le dossier médical n'existe pas pour {} {}.", updatedMedicalrecord.getFirstName(),
					updatedMedicalrecord.getLastName());
			throw new MedicalrecordNotFoundException("Le dossier médical n'existe pas pour cette personne.");
		}
		logger.info("Mise à jour du dossier médical pour {} {}.", updatedMedicalrecord.getFirstName(),
				updatedMedicalrecord.getLastName());
		medicalrecordRepository.updateMedicalrecord(updatedMedicalrecord);
		logger.info("Le dossier médical de {} {} a été mis à jour avec succès.",
				updatedMedicalrecord.getFirstName(), updatedMedicalrecord.getLastName());
	}

}

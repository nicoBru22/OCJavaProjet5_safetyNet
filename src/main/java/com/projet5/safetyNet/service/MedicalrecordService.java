package com.projet5.safetyNet.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.projet5.safetyNet.Exception.InvalidRequestException;
import com.projet5.safetyNet.Exception.MedicalRecordExistException;
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
		logger.info("MedicalrecordService, initialisé avec succès.");
	}

	/**
	 * Récupération de la liste des dossiers médicaux.
	 *
	 * Cette méthode retourne l'ensemble des dossiers médicaux enregistrés. Elle
	 * journalise l'opération et avertit si aucun dossier médical n'est disponible.
	 *
	 * @return une liste de {@code Medicalrecord}, pouvant être vide si aucun
	 *         dossier médical n'est trouvé.
	 * @see MedicalrecordRepository#getAllMedicalrecord()
	 */
	public List<Medicalrecord> getAllMedicalrecord() {
		logger.debug("Récupération de la liste des dossiers médicaux.");
		List<Medicalrecord> medicalrecords = medicalrecordRepository.getAllMedicalrecord();
		if (medicalrecords.isEmpty()) {
			logger.error("La liste de dossiers médicaux est vide.");
		}
		logger.info("{} dossiers médicaux récupérés.", medicalrecords.size());
		return medicalrecords;
	}

	/**
	 * Ajoute un nouveau dossier médical.
	 *
	 * Cette méthode vérifie la validité des informations du dossier médical avant
	 * de l'ajouter. Elle s'assure que les champs prénom, nom et date de naissance
	 * sont renseignés, et qu'aucun dossier médical identique n'existe déjà dans la
	 * base de données.
	 *
	 * @param newMedicalrecord le dossier médical à ajouter
	 * @throws InvalidRequestException     si le prénom, le nom ou la date de
	 *                                     naissance est manquant
	 * @throws MedicalRecordExistException si un dossier médical existe déjà pour
	 *                                     cette personne
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
			throw new MedicalRecordExistException("Un dossier médical existe déjà pour cette personne.");
		}

		logger.debug("Ajout d'un nouveau dossier médical pour {} {}.", newMedicalrecord.getFirstName(),
				newMedicalrecord.getLastName());
		medicalrecordRepository.addMedicalrecord(newMedicalrecord);
		logger.info("Le dossier médical de {} {} a été ajouté avec succès.", newMedicalrecord.getFirstName(),
				newMedicalrecord.getLastName());
	}

	/**
	 * Supprime un dossier médical.
	 *
	 * Cette méthode vérifie la validité des informations fournies avant de
	 * supprimer le dossier médical correspondant. Elle s'assure que les champs
	 * prénom et nom sont renseignés et que le dossier médical existe dans la base
	 * de données.
	 *
	 * @param deletedMedicalrecord le dossier médical à supprimer
	 * @throws InvalidRequestException        si le prénom ou le nom est manquant
	 * @throws MedicalrecordNotFoundException si aucun dossier médical correspondant
	 *                                        n'est trouvé
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

		logger.debug("Suppression du dossier médical pour {} {}.", deletedMedicalrecord.getFirstName(),
				deletedMedicalrecord.getLastName());
		medicalrecordRepository.deleteMedicalrecord(deletedMedicalrecord);
		logger.info("Le dossier médical de {} {} a été supprimé avec succès.", deletedMedicalrecord.getFirstName(),
				deletedMedicalrecord.getLastName());
	}

	/**
	 * Met à jour un dossier médical existant.
	 *
	 * Cette méthode vérifie la validité des informations fournies avant de procéder
	 * à la mise à jour du dossier médical correspondant. Elle s'assure que les
	 * champs prénom et nom sont renseignés et que le dossier médical existe dans la
	 * base de données.
	 *
	 * @param updatedMedicalrecord le dossier médical mis à jour
	 * @throws InvalidRequestException        si le prénom ou le nom est manquant
	 * @throws MedicalrecordNotFoundException si aucun dossier médical correspondant
	 *                                        n'est trouvé
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
		logger.debug("Mise à jour du dossier médical pour {} {}.", updatedMedicalrecord.getFirstName(),
				updatedMedicalrecord.getLastName());
		medicalrecordRepository.updateMedicalrecord(updatedMedicalrecord);
		logger.info("Le dossier médical de {} {} a été mis à jour avec succès.", updatedMedicalrecord.getFirstName(),
				updatedMedicalrecord.getLastName());
	}

}

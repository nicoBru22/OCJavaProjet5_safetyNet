package com.projet5.safetyNet.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.projet5.safetyNet.model.Medicalrecord;
import com.projet5.safetyNet.service.MedicalrecordService;

/**
 * Contrôleur REST pour la gestion des dossiers médicaux. *
 * 
 * Ce contrôleur fournit des points d'entrée pour :
 * <ul>
 * <li>Récupérer tout les dossiers médicaux.</li>
 * <li>Ajouter un nouveau dossier médical.</li>
 * <li>Mettre à jour un dossier médical.</li>
 * <li>Supprimer un dossier médical.</li>
 * </ul>
 * Il fait appel au {@link MedicalrecordService} pour effectuer les opérations
 * métier.
 */
@RestController
public class MedicalRecordController {

	private static final Logger logger = LoggerFactory.getLogger(MedicalRecordController.class);
	private final MedicalrecordService medicalrecordService;

	/**
	 * Constructeur du contrôleur des dossiers médicaux.
	 *
	 * @param medicalRecordService le service permettant de gérer les opérations sur
	 *                             les dossiers médicaux.
	 */
	public MedicalRecordController(MedicalrecordService medicalRecordService) {
		this.medicalrecordService = medicalRecordService;
	}

	/**
	 * Récupère la liste des dossiers médicaux.
	 * <p>
	 * Récupérer la liste de tous les dossiers médicaux à partir du endpoint
	 * /medicalrecords.
	 * </p>
	 *
	 * @return ResponseEntity contenant une liste des dossiers médicaux en cas de
	 *         succès ou un message d'erreur en cas d'échec.
	 */
	@GetMapping("/medicalrecords")
	public ResponseEntity<?> getMedicalRecords() {
		logger.info("Requête GET pour récupérer tous les dossiers médicaux.");
		try {
			List<Medicalrecord> medicalrecordList = medicalrecordService.getAllMedicalrecord();
			logger.info("La liste des dossiers médicaux a été récupérée avec succès.");
			logger.debug("Récupération réussie de {} dossiers médicaux.", medicalrecordList.size());
			return ResponseEntity.ok(medicalrecordList);
		} catch (Exception e) {
			logger.error("Erreur lors de la récupération des dossiers médicaux.", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erreur dans la récupération de la liste des dossiers médicaux.");
		}
	}

	/**
	 * Ajoute un novueau dossier médical.
	 * 
	 * <p>
	 * Cette méthode permet d'ajouter un nouveau dossier médical dans le système à
	 * partir de l'endpoint /medicalrecords.
	 * </p>
	 *
	 * @param newMedicalrecord le dossier médical à ajouter.
	 * @return ResponseEntity contenant un message de réussite en cas de succès ou
	 *         un message d'erreur en cas d'echec.
	 */
	@PostMapping("/medicalrecords")
	public ResponseEntity<?> addMedicalrecord(@RequestBody Medicalrecord newMedicalrecord) {
		logger.info("Requête POST pour ajouter un nouveau dossier médical : {}", newMedicalrecord);
		try {
			medicalrecordService.addMedicalrecord(newMedicalrecord);
			logger.info("Ajout réussi du dossier médical.");
			logger.debug("Le dossier médical {} a été ajouté avec succès", newMedicalrecord);
			return ResponseEntity.status(HttpStatus.CREATED).body("Le medicalrecord a été ajouté avec succès.");
		} catch (Exception e) {
			logger.error("Erreur lors de l'ajout d'un nouveau dossier médical.", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erreur dans l'ajout d'un nouveau medicalrecord.");
		}
	}

	/**
	 * Supprime un dossier médical.
	 * 
	 * <p>
	 * Cette méthode permet de supprimer un dossier médical du systeme à partir de
	 * ses données existante à l'endpoint /medicalrecords.
	 * </p>
	 *
	 * @param deletedMedicalrecord le dossier médical à supprimer.
	 * @return ResponseEntity contenant un message de réussite en cas de succès ou
	 *         un message d'erreur en cas d'echec.
	 */
	@DeleteMapping("/medicalrecords")
	public ResponseEntity<?> deleteMedicalrecord(@RequestBody Medicalrecord deletedMedicalrecord) {
		logger.info("Requête DELETE pour supprimer un dossier médical : {}", deletedMedicalrecord);
		try {
			medicalrecordService.deleteMedicalrecord(deletedMedicalrecord);
			logger.info("Suppression réussie du dossier médical.");
			logger.debug("Le dossier médical {} a été supprimé avec succès", deletedMedicalrecord);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Le medicalrecord a été supprimé avec succès.");
		} catch (Exception e) {
			logger.error("Erreur lors de la suppression du dossier médical.", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erreur lors de la suppression du medicalrecord.");
		}
	}

	/**
	 * Met à jour un dossier médical.
	 * 
	 * <p>
	 * Cette méthode met à jour un dossier médical existant à partir de l'endpoint
	 * /medicalrecord.
	 * </p>
	 * 
	 * @param updatedMedicalrecord le dossier médical contenant les nouvelles
	 *                             informations.
	 * @return ResponseEntity contenant un message de réussite en cas de succès ou
	 *         un message d'erreur en cas d'echec.
	 */
	@PutMapping("/medicalrecords")
	public ResponseEntity<?> updateMedicalrecord(@RequestBody Medicalrecord updatedMedicalrecord) {
		logger.info("Requête PUT pour mettre à jour un dossier médical : {}", updatedMedicalrecord);
		try {
			medicalrecordService.updateMedicalrecord(updatedMedicalrecord);
			logger.info("Mise à jour réussie du dossier médical.");
			logger.debug("Le dossier médical {} a été mise à jour avec succès", updatedMedicalrecord);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Le medicalrecord a été modifié avec succès.");
		} catch (Exception e) {
			logger.error("Erreur lors de la mise à jour du dossier médical.", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erreur lors de la modification du medicalrecord.");
		}
	}
}
